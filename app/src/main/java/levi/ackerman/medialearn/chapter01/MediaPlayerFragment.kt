package levi.ackerman.medialearn.chapter01

import android.media.MediaPlayer
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.Button
import levi.ackerman.medialearn.BaseFragment
import levi.ackerman.medialearn.R

class MediaPlayerFragment : BaseFragment() {
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
            val surfaceView = SurfaceView(activity)
            surfaceViewContainer.addView(surfaceView)
            surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setDisplay(holder)
                    mediaPlayer?.setDataSource("http://10.68.108.47:8000/mirror.mp4")
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