package com.example.guessthesong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pop_up.*
import kotlin.math.roundToInt

class PopUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_pop_up)

        val songLyric = intent.getStringExtra("LYRIC")
        val congratulations = "Congratulations you have found a new lyric! \n\n"
        //Toast.makeText(this, R.string.congratulations, Toast.LENGTH_SHORT)
        congratulationsMsg.setText(congratulations + songLyric)
        var dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width: Int = (dm.widthPixels * 0.6).roundToInt()
        val height: Int = (dm.heightPixels * 0.6).roundToInt()

        window.setLayout(width, height)
    }
}
