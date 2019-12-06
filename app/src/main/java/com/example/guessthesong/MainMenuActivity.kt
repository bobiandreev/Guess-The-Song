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


    /**
     * Method called whenever the Main Menu activity is called to the foreground. Used to set up the
     * layout and initialize the listners for the buttons and toggles.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        setSupportActionBar(toolbar)

        FileReaderObject.loadClassicSong(applicationContext)
        FileReaderObject.loadModernSong(applicationContext)

        /**
         * Starts the Song History activity.
         */
        SongsHistoryButton.setOnClickListener {
            var intent = Intent(applicationContext, SongHistoryActivity::class.java)
            startActivity(intent)
        }

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        /**
         * Listener for the state of the mode toggle.
         */
        modeSwitch.setOnClickListener {
            mode = modeSwitch.isChecked
        }

        /**
         * Listener for the play button which starts the Map activity.
         */
        playButton.setOnClickListener {
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
        }
        textViewTotalPoints.setText(points.toString())
    }

    /**
     * Called whenever the activity is restarted after being paused. Updates the total points.
     */
    override fun onResume() {
        super.onResume()
        textViewTotalPoints.setText(points.toString())
    }

    companion object MainMenuCompanion {
        private var points : Int = 0
        private var mode: Boolean = false

        /**
         * Gets the game mode selected.
         * @return The game mode selected.
         */
        fun getMode(): Boolean {
            return mode
        }

        /**
         * Updates the points on a successful guess.
         * @param addition The number of points to be added to the total.
         */
        fun updatePoints (addition : Int){
            points += addition
        }
    }

}
