package levi.ackerman.medialearn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment(){
    abstract @LayoutRes fun getLayoutId():Int

    protected lateinit var rootView :View

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView =  inflater.inflate(getLayoutId(), container, false)
        initData()
        return rootView
    }

    open fun initData() {

    }
}
