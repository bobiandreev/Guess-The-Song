package com.example.guessthesong

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.content_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        setSupportActionBar(toolbar)
        FileReaderObject.loadSongs(applicationContext)
        SongsHistoryButton.setOnClickListener {

        }

        modeSwitch.setOnClickListener {
            Toast.makeText(this, modeSwitch.isChecked.toString(), Toast.LENGTH_SHORT).show()
            mode = modeSwitch.isChecked
        }
        playButton.setOnClickListener {
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    companion object ModeReader {
        private var mode: Boolean = false
        fun getMode(): Boolean {
            return mode
        }
    }

}
