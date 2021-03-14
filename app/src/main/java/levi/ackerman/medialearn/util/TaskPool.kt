package levi.ackerman.medialearn.util

import android.os.Handler
import android.os.Looper
import levi.ackerman.medialearn.util.TASK_TYPE.MAIN
import java.util.concurrent.Executors

object TaskPool {
    private val executor = Executors.newScheduledThreadPool(4)
    private val mainHandler = Handler(Looper.getMainLooper())
    fun post(taskType: TASK_TYPE, task: () -> Unit) {
        when (taskType) {
            MAIN -> {
                mainHandler.post(task)
            }
            else -> {
                executor.execute(task)
            }
        }
    }
}

enum class TASK_TYPE {
    BACKGROUND, MAIN, IO
}