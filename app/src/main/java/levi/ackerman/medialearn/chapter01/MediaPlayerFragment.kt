package levi.ackerman.medialearn.chapter01

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import levi.ackerman.medialearn.BaseFragment
import levi.ackerman.medialearn.R
import java.util.HashMap

class MediaPlayerFragment : BaseFragment() {
    companion object {
        const val PATH = "https://www.w3school.com.cn/example/html5/mov_bbb.mp4"
    }

    private lateinit var surfaceViewContainer: ViewGroup
    private lateinit var playButton: Button
    private lateinit var stopButton: Button
    private var mediaPlayer: MediaPlayer? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_01_mediaplayer
    }

    override fun initData() {
        playButton = rootView.findViewById(R.id.btn_play)
        stopButton = rootView.findViewById(R.id.stop_play)
        surfaceViewContainer = rootView.findViewById(R.id.container_sv_player)
        playButton.setOnClickListener {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(PATH, HashMap()) //如果是网络文件，需要hashmap来传递头参数，否则会抛异常
            val videoWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
            val videoHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
            val videoDuration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val realWidth = surfaceViewContainer.width
            val readHeight = videoHeight * realWidth / videoWidth
            val surfaceView = SurfaceView(activity)
            surfaceViewContainer.addView(surfaceView,FrameLayout.LayoutParams(realWidth, readHeight))
            surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setDisplay(holder)
                    mediaPlayer?.setDataSource(PATH)
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
            })
        }
        stopButton.setOnClickListener {
            surfaceViewContainer.removeAllViews()
        }
    }
}