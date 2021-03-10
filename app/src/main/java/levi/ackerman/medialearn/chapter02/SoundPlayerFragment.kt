package levi.ackerman.medialearn.chapter02

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import levi.ackerman.medialearn.BaseFragment
import levi.ackerman.medialearn.R
import java.io.File
import java.io.FileInputStream
import kotlin.concurrent.thread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        btnPlayAudioTrack.setOnClickListener{
            playWithAudioTrack()
        }
    }

    private fun playWithOpenSL() {
    }

    private fun playWithAudioTrack() {
        val audioAttributes = AudioAttributes.Builder().build()
        val sampleRate = 44100
        val format =
            AudioFormat.Builder().setSampleRate(sampleRate).setEncoding(AudioFormat.ENCODING_PCM_16BIT).build()
        val bufferSize =
            AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT)
//        val audioTrack = AudioTrack(
//            audioAttributes,
//            format,
//            bufferSize,
//            AudioTrack.MODE_STREAM,
//            AudioManager.AUDIO_SESSION_ID_GENERATE
//        )

        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM
        )

        audioTrack.play()
        object :Thread(){
            override fun run() {
                super.run()
                val fis = activity!!.assets.open("yura.pcm")
                val buf = ByteArray(128)
                var size = 0
                while(true){
                    size = fis.read(buf)
                    if (size <= 0){
                        break
                    }
                    audioTrack.write(buf,0,size)
                }
                fis.close()
            }
        }.start()
    }
}