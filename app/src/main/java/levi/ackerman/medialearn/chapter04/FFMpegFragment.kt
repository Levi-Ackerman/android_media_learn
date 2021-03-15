package levi.ackerman.medialearn.chapter04

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import levi.ackerman.medialearn.BaseFragment
import levi.ackerman.medialearn.R
import levi.ackerman.medialearn.util.LogUtil
import levi.ackerman.medialearn.util.TASK_TYPE.BACKGROUND
import levi.ackerman.medialearn.util.TaskPool

/**
 * A simple [Fragment] subclass.
 * Use the [FFMpegFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FFMpegFragment : BaseFragment() {
    init {
        System.loadLibrary("native-lib")
    }

    override fun getLayoutId() = R.layout.fragment_f_f_mpeg

    override fun initData() {
        rootView.findViewById<Button>(R.id.btn_play).setOnClickListener {

        }
    }
}