package com.example.guessthesong

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.content_main_menu.*

class MainMenuActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        setSupportActionBar(toolbar)
        FileReaderObject.loadClassicSong(applicationContext)
        FileReaderObject.loadModernSong(applicationContext)


        SongsHistoryButton.setOnClickListener {
            var intent = Intent(applicationContext, SongHistoryActivity::class.java)
            startActivity(intent)
        }


        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        modeSwitch.setOnClickListener {
            mode = modeSwitch.isChecked
        }

        playButton.setOnClickListener {
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
        }
        textViewTotalPoints.setText(points.toString())
    }

    override fun onResume() {
        super.onResume()
        textViewTotalPoints.setText(points.toString())
    }
    companion object MainMenuCompanion {
        private var points : Int = 0
        private var mode: Boolean = false
        fun getMode(): Boolean {
            return mode
        }

        fun updatePoints (addition : Int){
            points += addition
        }
    }

}
