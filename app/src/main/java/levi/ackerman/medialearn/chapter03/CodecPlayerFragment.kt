package levi.ackerman.medialearn.chapter03

import android.media.MediaCodec
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
import levi.ackerman.medialearn.util.LogUtil
import levi.ackerman.medialearn.util.TASK_TYPE.BACKGROUND
import levi.ackerman.medialearn.util.TaskPool

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
    }

    private lateinit var surfaceViewContainer: ViewGroup
    private lateinit var playButton: Button
    private lateinit var stopButton: Button
    private lateinit var surfaceView: SurfaceView

    private lateinit var mediaFileName: String
    private lateinit var mediaExtractor: MediaExtractor
    private val mediaInfo: MediaInfo = MediaInfo()

    private fun log(text: String) {
        LogUtil.i(this.javaClass.simpleName, text)
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                EVENT_CLICK_PLAY -> {
                    log("onClickPlayButton")
                    prepareMediaInfo()
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

            }
        }
        handler.sendEmptyMessage(EVENT_PREPARED_SUCCESS)
    }

    private fun onSurfaceViewCreated() {
        if (mediaInfo.audioInfo != null) {
            val audioCodec = MediaCodec.createDecoderByType(mediaInfo.audioInfo!!.mineType)
            audioCodec.configure(mediaInfo.audioInfo!!.mediaFormat, null, null, 0)
            audioCodec.start()
            log("audio codec is started !")
            TaskPool.post(BACKGROUND) {
                while (true) {
                    val index = audioCodec.dequeueInputBuffer(-1)
                    if (index < 0) {
                        Thread.sleep(10)
                        continue
                    }
                    val inputBuffer = audioCodec.getInputBuffer(index)
                    if (inputBuffer == null) {
                        Thread.sleep(10)
                        continue
                    }
                    mediaExtractor.selectTrack(mediaInfo.audioInfo!!.trackIndex)
                    val length = mediaExtractor.readSampleData(inputBuffer, 0)
                    if (length < 0) {
                        //读到末尾了，给一个end标记位给解码器
                        audioCodec.queueInputBuffer(index, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        break
                    }
                    audioCodec.queueInputBuffer(index, 0, length, mediaExtractor.sampleTime, 0)
                    mediaExtractor.advance()
                }
            }
            TaskPool.post(BACKGROUND) {
                while (true) {
                    val bufferInfo = MediaCodec.BufferInfo()
                    bufferInfo.flags = 0
                    val index = audioCodec.dequeueOutputBuffer(bufferInfo, -1)
                    if (bufferInfo.flags.and(MediaCodec.BUFFER_FLAG_END_OF_STREAM) == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                        //结束了
                        audioCodec.stop()
                        break
                    }
                    if (index < 0) {
                        Thread.sleep(10)
                        continue
                    }
                    val outputBuffer = audioCodec.getOutputBuffer(index)
                    audioCodec.releaseOutputBuffer(index, false)

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
        playButton.setOnClickListener {
            handler.sendEmptyMessage(EVENT_CLICK_PLAY)
        }
        mediaFileName = arguments?.getString("file") ?: throw IllegalArgumentException()
    }
}