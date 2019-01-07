package org.example.ankodemo

import android.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import me.lightspeed7.fireflies.audio.OneTimeBuzzer
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk25.coroutines.onClick

class FlashingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FlashingActivityUi().setContentView(this)
    }

//    fun process(ui: AnkoContext<FlashingActivity>, flashes: Int, flashLength: Int, flashInterval: Int) {
//        ui.doAsync {
//            Thread.sleep(500)
//
//            activityUiThreadWithContext {
//                if (checkCredentials(name.toString(), password.toString())) {
//                    toast("Logged in! :)")
//                    startActivity<FlashingActivity>()
//                } else {
//                    toast("Wrong password :( Enter user:password")
//                }
//            }
//        }
//    }
//
//    private fun checkCredentials(name: String, password: String) = name == "user" && password == "password"
}


class FlashingActivityUi : AnkoComponent<FlashingActivity> {
    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 20f
            is EditText -> v.textSize = 20f
        }
    }

    override fun createView(ui: AnkoContext<FlashingActivity>) = with(ui) {

        val flashData = FlashingState()
        val headerLabelHeight: Float = 20f

        val counts: Set<Int> = setOf(1, 2, 5)
        val times: Set<Int> = setOf(1, 5, 10, 50, 100, 500, 1000)

        linearLayout {
            padding = dip(18)

            //
            // Flashes
            // //////////////////////////////
            verticalLayout {

                val width: Int = 150

                textView {
                    setWidth(width)
                    textSize = headerLabelHeight
                    text = "# flashes"
                }

                val flashesText = editText() {
                    textSize = headerLabelHeight
                    setWidth(width)
                    setText(flashData.flashes.toString())
                }

                counts.reversed().map { it ->
                    val pos: Int = it
                    val neg: Int = 0 - it

                    linearLayout {
                        button(neg.toString()) {
                            setWidth(width / 2)
                            onClick { flashesText.setText(flashData.updateFlashes(neg).toString()) }
                        }
                        button("+" + it.toString()) {
                            setWidth(width / 2)
                            onClick { flashesText.setText(flashData.updateFlashes(pos).toString()) }
                        }
                    }
                }
            }.lparams(width = wrapContent) {
                horizontalMargin = dip(5)
            }

            //
            // Duration
            // /////////////////////////////////
            verticalLayout {

                val width: Int = 200

                textView {
                    setWidth(width)
                    textSize = headerLabelHeight
                    text = "Duration(ms)"
                }

                val durationText = editText() {
                    textSize = headerLabelHeight
                    setWidth(width)
                    setText(flashData.duration.toString())
                }

                times.reversed().map { it ->
                    val pos: Int = it
                    val neg: Int = 0 - it

                    linearLayout {
                        button(neg.toString()) {
                            setWidth(width / 2)
                            onClick { durationText.setText(flashData.updateDuration(neg).toString()) }
                        }
                        button("+" + it.toString()) {
                            setWidth(width / 2)
                            onClick { durationText.setText(flashData.updateDuration(pos).toString()) }
                        }
                    }
                }
            }.lparams(width = wrapContent) {
                horizontalMargin = dip(5)
            }

            //
            // Pause
            // /////////////////////////////////
            verticalLayout {

                val width: Int = 200

                textView {
                    setWidth(width)
                    textSize = headerLabelHeight
                    text = "Pause(ms)"
                }

                val pauseText = editText() {
                    textSize = headerLabelHeight
                    setWidth(width)
                    setText(flashData.pause.toString())
                }

                times.reversed().map { it ->
                    val pos: Int = it
                    val neg: Int = 0 - it

                    linearLayout {
                        button(neg.toString()) {
                            setWidth(width / 2)
                            onClick { pauseText.setText(flashData.updatePause(neg).toString()) }
                        }
                        button("+" + it.toString()) {
                            setWidth(width / 2)
                            onClick { pauseText.setText(flashData.updatePause(pos).toString()) }
                        }
                    }
                }
            }.lparams(width = wrapContent) {
                horizontalMargin = dip(5)
            }

            //
            // Interval
            // /////////////////////
            relativeLayout {

                val width: Int = 450

                verticalLayout {

                    textView {
                        setWidth(width)
                        textSize = headerLabelHeight
                        text = "Interval(ms)"
                    }

                    val intervalText = editText() {
                        textSize = headerLabelHeight
                        setWidth(width)
                        setText(flashData.interval().toString())
                    }

                    flashData.intervalText = intervalText

                    textView {
                        setWidth(width)
                        textSize = headerLabelHeight
                        text = "FlashType"
                    }

                }.lparams(width = wrapContent) {
                    alignParentTop()
                }

                button("Execute") {
                    horizontalPadding = dip(125)
                    verticalPadding = dip(50)
                    onClick {
                        val msg = "Flashes ${flashData.flashes}  Duration: ${flashData.duration}  Pause: ${flashData.pause}  Interval: ${flashData.interval()}"
                        toast(msg)
                        OneTimeBuzzer().play()
                        toast("Audio done!")
                    }

                }.lparams(width = wrapContent) {
                    alignParentBottom()
                }

                //
            }.lparams(width = wrapContent) {
                horizontalMargin = dip(5)
            }

        }.applyRecursively(customStyle)
    }
}