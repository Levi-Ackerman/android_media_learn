package levi.ackerman.medialearn.chapter03

import android.media.AudioTrack
import android.media.AudioTrack.OnPlaybackPositionUpdateListener
import android.media.MediaCodec
import android.media.MediaCodec.BufferInfo
import android.media.MediaCodec.createDecoderByType
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import levi.ackerman.medialearn.BaseFragment
import levi.ackerman.medialearn.R
import levi.ackerman.medialearn.chapter02.AudioPlayer
import levi.ackerman.medialearn.util.LogUtil
import levi.ackerman.medialearn.util.TASK_TYPE.BACKGROUND
import levi.ackerman.medialearn.util.TaskPool
import levi.ackerman.medialearn.util.showToast
import java.io.File
import java.nio.ByteBuffer

/**
 * A simple [Fragment] subclass.
 * Use the [CodecPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CodecPlayerFragment : BaseFragment() {

    companion object {
        const val EVENT_CLICK_PLAY = 0
        const val EVENT_SURFACEVIEW_CREATED = 1
        const val EVENT_PREPARED_SUCCESS = 2
        const val EVENT_CLICK_PAUSE = 3
    }

    private var lastEvent = -1

    private lateinit var surfaceViewContainer: ViewGroup
    private lateinit var playButton: Button
    private lateinit var stopButton: Button
    private lateinit var surfaceView: SurfaceView

    private lateinit var mediaFileName: String
    private lateinit var mediaExtractor: MediaExtractor
    private val mediaInfo: MediaInfo = MediaInfo()
    private lateinit var audioCodec: MediaCodec
    private lateinit var videoCodec: MediaCodec
    private lateinit var audioTrack: AudioTrack

    private var running = false

    private fun log(text: String) {
        LogUtil.i(this.javaClass.simpleName, text)
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                EVENT_CLICK_PAUSE -> {
                    running = false
                    audioTrack.pause()
                }
                EVENT_CLICK_PLAY -> {
                    log("onClickPlayButton")
                    if (lastEvent == -1) {
                        prepareMediaInfo()
                    } else {
                        audioTrack.play()
                        startCodec()
                    }
                }
                EVENT_PREPARED_SUCCESS -> {
                    log("onPrepareMediaInfo SUCCESS")
                    createSurfaceView()
                }
                EVENT_SURFACEVIEW_CREATED -> {
                    log("onSurfaceViewCreated")
                    onSurfaceViewCreated()
                }
                else -> {

                }
            }
            lastEvent = msg.what
        }
    }

    private fun prepareMediaInfo() {
        if (!File(mediaFileName).exists()) {
            activity!!.showToast("文件不存在，请先初始化")
            return
        }
        mediaExtractor = MediaExtractor()
        mediaExtractor.setDataSource(mediaFileName)
        for (i in 0 until mediaExtractor.trackCount) {
            val format = mediaExtractor.getTrackFormat(i)
            LogUtil.i("extractor", "format stream $i: ${format.toString()}")
            val mineType = format.getString(MediaFormat.KEY_MIME)
            if (mineType?.startsWith("audio/") == true) {
                //audio轨道
                val sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
                val channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
                mediaInfo.audioInfo = AudioInfo(format, i, mineType, sampleRate, channelCount)
            } else if (mineType?.startsWith("video/") == true) {
                val frameRate = format.getInteger(MediaFormat.KEY_FRAME_RATE)
                val width = format.getInteger(MediaFormat.KEY_WIDTH)
                val height = format.getInteger(MediaFormat.KEY_HEIGHT)
                mediaInfo.videoInfo = VideoInfo(format, i, mineType, width, height, frameRate)
            }
        }
        handler.sendEmptyMessage(EVENT_PREPARED_SUCCESS)
    }

    private fun onSurfaceViewCreated() {
        if (mediaInfo.audioInfo != null) {
            audioCodec = MediaCodec.createDecoderByType(mediaInfo.audioInfo!!.mineType)
            audioCodec.configure(mediaInfo.audioInfo!!.mediaFormat, null, null, 0)
            audioCodec.start()
            val audioPlayer = AudioPlayer()
            log("audio codec is started !")
            audioTrack = audioPlayer.playWithAudioTrack(activity!!, mediaInfo.audioInfo!!.sampleRate)
            audioTrack.play()

            videoCodec = createDecoderByType(mediaInfo.videoInfo!!.mineType)
            videoCodec.configure(mediaInfo.videoInfo!!.mediaFormat, null, null, 0)
            videoCodec.start()
            startCodec()
        }
    }

    private fun startCodec() {
        running = true
        TaskPool.post(BACKGROUND) {
            var audioFinish = false
            var videoFinish = false
            while (running) {
                var audioReady = false
                var videoReady = false
                if (!audioFinish) {
                    mediaExtractor.selectTrack(mediaInfo.audioInfo!!.trackIndex)
                    val audioIndex = audioCodec.dequeueInputBuffer(-1)
                    audioReady = audioIndex >= 0
                    if (audioReady) {
                        val inputBuffer = audioCodec.getInputBuffer(audioIndex)
                        inputBuffer!!.clear()
                        val length = mediaExtractor.readSampleData(inputBuffer, 0)
                        if (length < 0) {
                            //读到末尾了，给一个end标记位给解码器
                            audioCodec.queueInputBuffer(audioIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                            audioFinish = true
                        }
                        audioCodec.queueInputBuffer(audioIndex, 0, length, mediaExtractor.sampleTime, 0)
                        mediaExtractor.advance()
                    }
                }
//                if (!videoFinish) {
//                    mediaExtractor.selectTrack(mediaInfo.videoInfo!!.trackIndex)
//                    val videoIndex = videoCodec.dequeueInputBuffer(-1)
//                    videoReady = videoIndex >= 0
//                    if (videoReady) {
//                        val inputBuffer = videoCodec.getInputBuffer(videoIndex)
//                        inputBuffer!!.clear()
//                        val length = mediaExtractor.readSampleData(inputBuffer, 0)
//                        if (length < 0) {
//                            videoCodec.queueInputBuffer(videoIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
//                            videoFinish = true
//                        }
//                        videoCodec.queueInputBuffer(videoIndex, 0, length, mediaExtractor.sampleTime, 0)
//                        mediaExtractor.advance()
//                    }
//                }

                if (videoFinish && audioFinish) {
                    break
                }
                if (!audioReady && !videoReady) {
                    Thread.sleep(10)
                }
            }
        }
        TaskPool.post(BACKGROUND) {
            var audioFinish = false
            var videoFinish = false
            while (running) {
                var audioReady = false
                var videoReady = false
                if (!audioFinish) {
                    val bufferInfo = BufferInfo()
                    bufferInfo.flags = 0
                    val index = audioCodec.dequeueOutputBuffer(bufferInfo, -1)
                    if (bufferInfo.flags.and(MediaCodec.BUFFER_FLAG_END_OF_STREAM) == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                        //结束了
                        audioCodec.stop()
                        audioFinish = true
                    }
                    audioReady = index >= 0
                    if (audioReady) {
                        val outputBuffer = audioCodec.getOutputBuffer(index)
                        audioTrack.write(outputBuffer!!, bufferInfo.size, AudioTrack.WRITE_BLOCKING)
                        audioCodec.releaseOutputBuffer(index, false)
                    }
                }
//                if (!videoFinish) {
//                    val bufferInfo = BufferInfo()
//                    bufferInfo.flags = 0
//                    val index = videoCodec.dequeueOutputBuffer(bufferInfo, -1)
//                    if (bufferInfo.flags.and(MediaCodec.BUFFER_FLAG_END_OF_STREAM) == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
//                        videoCodec.stop()
//                        videoFinish = true
//                    }
//                    videoReady = index >= 0
//                    if (videoReady) {
//                        val outputBuffer = videoCodec.getOutputBuffer(index)
//                        LogUtil.i("lee111","${bufferInfo.offset}-${bufferInfo.size}")
//                        videoCodec.releaseOutputBuffer(index, false)
//                    }
//                }
                if (videoFinish && audioFinish){
                    break
                }
                if (!videoReady && !audioReady){
                    Thread.sleep(10)
                }
            }
        }
    }

    private fun createSurfaceView() {
        surfaceView = SurfaceView(activity)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                handler.sendEmptyMessage(EVENT_SURFACEVIEW_CREATED)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }
        })
        surfaceViewContainer.addView(surfaceView)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_codec_player
    }

    override fun initData() {
        playButton = rootView.findViewById(R.id.btn_play)
        stopButton = rootView.findViewById(R.id.stop_play)
        surfaceViewContainer = rootView.findViewById(R.id.container_sv_player)
        stopButton.setOnClickListener {
            handler.sendEmptyMessage(EVENT_CLICK_PAUSE)
        }
        playButton.setOnClickListener {
            handler.sendEmptyMessage(EVENT_CLICK_PLAY)
        }
        mediaFileName = arguments?.getString("file") ?: throw IllegalArgumentException()
    }
}