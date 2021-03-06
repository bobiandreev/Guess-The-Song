package com.example.guessthesong

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pop_up.*
import kotlin.math.roundToInt

class PopUpActivity : AppCompatActivity() {

    /**
     * Called whenever the Pop Up activity is started. It sets the text displayed on the pop up and
     * its form.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_pop_up)

        window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        val message = intent.getStringExtra("STRING")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        congratulationsMsg.setText(message)

        var dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        val width: Int = (dm.widthPixels * 0.6).roundToInt()
        val height: Int = (dm.heightPixels * 0.6).roundToInt()

        window.setLayout(width, height)
    }
}
