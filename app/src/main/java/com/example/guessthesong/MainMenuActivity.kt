package com.example.guessthesong

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.content_main_menu.*

class MainMenuActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        setSupportActionBar(toolbar)

//        SongsHistoryButton.setOnClickListener {
//            val intent = Intent(applicationContext, SongManagerTesting::class.java)
//            startActivity(intent)
//
//        }

        playButton.setOnClickListener {
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
            readFiles(applicationContext)
        }
    }

    companion object FileReader {
        private  val classics = "Classic"
        private  val modern = "Current"
        private lateinit var classicsContent : MutableList<String>
        private lateinit var modernContent : MutableList<String>
        private lateinit var currentSong: MutableList<String>
        private lateinit var classicSong : MutableList<String>
        private lateinit var classicSongName : String
        private lateinit var currentSongName : String

        fun readFiles(context: Context) {
            classicsContent = context.assets.list(classics)!!.toMutableList()
            modernContent = context.assets.list(modern)!!.toMutableList()

            classicSongName = classicsContent!!.random()
            currentSongName = modernContent!!.random()
            classicSong =
                context.assets.open(classics + "/" + classicSongName)
                    .bufferedReader().readLines().toMutableList()
            currentSong =
                context.assets.open(modern + "/" + currentSongName)
                    .bufferedReader().readLines().toMutableList()

            classicsContent.remove(classicSongName)
            modernContent.remove(currentSongName)

            val nextLineClass = nextLineClassic((classicSong))
            val nextLineCurr = nextLineCurrent((currentSong))
            classicSong.remove(nextLineClass)
            currentSong.remove(nextLineCurr)
            println("Classic: " + nextLineClass)
            println("Current: " + nextLineCurr)
        }

        fun nextLineClassic(classicSong: MutableList<String>): String {
            return classicSong.random()

        }

        fun nextLineCurrent(currentSong: MutableList<String>): String {
            return currentSong.random()

        }
    }

}
