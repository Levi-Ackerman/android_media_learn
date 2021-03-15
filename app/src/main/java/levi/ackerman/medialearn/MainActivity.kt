package levi.ackerman.medialearn

import android.R.id
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import levi.ackerman.medialearn.R.layout
import levi.ackerman.medialearn.chapter01.MediaPlayerFragment
import levi.ackerman.medialearn.chapter02.SoundPlayerFragment
import levi.ackerman.medialearn.chapter03.CodecPlayerFragment
import levi.ackerman.medialearn.chapter04.FFMpegFragment
import levi.ackerman.medialearn.util.showToast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var outMp4FileName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        outMp4FileName = "${getExternalFilesDir("media")?.absolutePath}/yura.mp4"
    }

    fun initParams(view: View?) {
        val outMp4 = File(outMp4FileName)
        if (outMp4.exists()) {
            showToast("mp4文件已经复制")
            return
        }
        var inputStream: InputStream? = null
        var fos: OutputStream? = null
        try {
            inputStream = assets.open("yura.mp4")
            fos = FileOutputStream(outMp4)
            val buf = ByteArray(512)
            while (true) {
                val length = inputStream.read(buf)
                if (length <= 0) {
                    break
                }
                fos.write(buf, 0, length)
            }
            showToast("完成文件复制")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            fos?.close()
        }
    }

    fun jumpChapter01(view: View?) {
        jumpFragment(MediaPlayerFragment::class.java)
    }

    fun jumpChapter02(view: View?) {
        jumpFragment(SoundPlayerFragment::class.java)
    }

    fun jumpChapter03(view: View?) {
        jumpFragment(CodecPlayerFragment::class.java, Bundle().apply { putString("file", outMp4FileName) })
    }

    private fun jumpFragment(fragmentClazz: Class<out BaseFragment>, data: Bundle? = null) {
        supportFragmentManager.beginTransaction().replace(
            id.content,
            Fragment.instantiate(this, fragmentClazz.name, data))
            .addToBackStack(fragmentClazz.name).commit()
    }

    fun jumpChapter04(view: View) {
        jumpFragment(FFMpegFragment::class.java)
    }
}