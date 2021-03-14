package levi.ackerman.medialearn.chapter02

import android.media.AudioTrack
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import levi.ackerman.medialearn.BaseFragment
import levi.ackerman.medialearn.R

/**
 * A simple [Fragment] subclass.
 * Use the [SoundPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoundPlayerFragment : BaseFragment() {

    private lateinit var btnPlayOpenSL: Button
    private lateinit var btnPlayAudioTrack: Button

    override fun getLayoutId(): Int {
        return R.layout.fragment_sound_player
    }

    override fun initData() {
        super.initData()
        btnPlayAudioTrack = rootView.findViewById<Button>(R.id.btn_play_audio_track)
        btnPlayOpenSL = rootView.findViewById<Button>(R.id.btn_play_audio_opensl)
        btnPlayAudioTrack.setOnClickListener {
            val audioTrack = AudioPlayer().playWithAudioTrack(activity!!)


            if (audioTrack.state == AudioTrack.STATE_UNINITIALIZED) {
                Toast.makeText(context!!, "AudioTrack初始化失败!", Toast.LENGTH_SHORT).show()
            }else {
                audioTrack.play()
                object : Thread() {
                    override fun run() {
                        super.run()
                        val fis = context!!.assets.open("yura.pcm")
                        val buf = ByteArray(128)
                        var size = 0
                        while (true) {
                            size = fis.read(buf)
                            if (size <= 0) {
                                break
                            }
                            audioTrack.write(buf, 0, size)
                        }
                        fis.close()
                        audioTrack.stop()
                        audioTrack.release()
                    }
                }.start()
            }
        }
        btnPlayOpenSL.setOnClickListener {
            playWithOpenSL()
        }
    }

    private fun playWithOpenSL() {
        BridgeUtil02.playAudioWithOpenSL()
    }
}