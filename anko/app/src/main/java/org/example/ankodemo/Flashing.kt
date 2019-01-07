package org.example.ankodemo

import android.widget.EditText
import android.os.AsyncTask


class FlashingState(
        var flashes: Int = 5,
        var duration: Int = 500,
        var pause: Int = 1000,
        var pattern: String? = "constant",
        var intervalText: EditText? = null
) //
{
    fun forcePositive(current: Int, delta: Int): Int {
        val temp = current + delta
        return if (temp < 0) 0 else temp
    }

    fun interval(): Int {
        return (duration.toString().toInt() + pause.toString().toInt())
    }

    fun updateFlashes(delta: Int): Int {
        flashes = forcePositive(flashes, delta)
        return flashes
    }

    fun updateDuration(delta: Int): Int {
        duration = forcePositive(duration, delta)
        intervalText?.setText(interval().toString())
        return duration
    }

    fun updatePause(delta: Int): Int {
        pause = forcePositive(pause, delta)
        intervalText?.setText(interval().toString())
        return pause
    }

    fun updatePattern(newPat: String): String {
        pattern = newPat
        return newPat
    }

    // https://stackoverflow.com/questions/2413426/playing-an-arbitrary-tone-with-android

    fun toFlashes() {
        generateSequence(1) { it + 1 }
                .take(flashes)
                .map { it: Int ->
                    ConstantFlash(it, duration, pause)
                }
    }

}

interface Flash {
    fun execute(): Unit
}

class ConstantFlash(val index:Int, duration: Int, pause: Int) : Flash {
    override fun execute(): Unit {
        return
    }
}


class FlashTask() : AsyncTask<Flash, Int, Long>() {

    override fun doInBackground(vararg steps: Flash): Long? {
        val count = steps.size
        var totalSize: Long = 0

        for ((index, value) in steps.withIndex()) {
            val progress: Int = (index / count.toFloat() * 100).toInt()

            println("The element at $index is $value")
            publishProgress(progress)
            if (isCancelled) break
        }

        return totalSize
    }

    protected fun onProgressUpdate(vararg values: Int) {
        println("foo")
    }

    override fun onPostExecute(result: Long?) {
        // showDialog("Downloaded $result bytes")
    }
}

