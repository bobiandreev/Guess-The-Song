package com.example.guessthesong

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_lyrics.*


class LyricsActivity : AppCompatActivity() {


    private var guess: AutoCompleteTextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics)
        setSupportActionBar(toolbar)


        Toast.makeText(this, FileReaderObject.getClassicSong(), Toast.LENGTH_SHORT).show()
        skipThisSongButton.setOnClickListener { view ->
            val message = "This song was \n"
            val intent = Intent(applicationContext, PopUpActivity::class.java)

            if (MainMenuActivity.getMode()) {
                intent.putExtra(
                    "STRING",
                    message + FileReaderObject.transformToNiceString(
                        FileReaderObject.getModernSong()
                    )
                )
                FileReaderObject.addToSongHistoryList(
                    Song(FileReaderObject.getModernSong(), "Current")
                )
                startActivity(intent)
                clearModernList()
                FileReaderObject.loadModernSong(applicationContext)
                loadList()
                resetModern()
            } else {
                intent.putExtra(
                    "STRING",
                    message + FileReaderObject.transformToNiceString(FileReaderObject.getClassicSong())
                )
                FileReaderObject.addToSongHistoryList(
                    Song(FileReaderObject.getClassicSong(), "Classic")

                )
                startActivity(intent)
                clearClassicList()
                FileReaderObject.loadClassicSong(applicationContext)
                loadList()
                resetClassic()
            }
        }

        val pointsForSong = resources.getString(R.string.points_for_song)
        if (MainMenuActivity.getMode()) {
            textViewPoints.setText(pointsForSong + modernSongPoints)
        } else {
            textViewPoints.setText(pointsForSong + classicSongPoints)
        }
        guess = findViewById(R.id.actv)

        guess!!.setOnItemClickListener { parent, arg1, pos, id ->

            val message = resources.getString(R.string.correct_song_message)
            val intentPopUp = Intent(applicationContext, PopUpActivity::class.java)
            intentPopUp.putExtra("STRING", message)

            if (MainMenuActivity.getMode()) {
                if (guess!!.text.toString().equals(
                        FileReaderObject.transformToNiceString(
                            FileReaderObject.getModernSong()
                        )
                    )
                ) {
                    FileReaderObject.addToSongHistoryList(
                        Song(
                            FileReaderObject.getModernSong(), "Current"
                        )
                    )

                    startActivity(intentPopUp)
                    clearModernList()
                    FileReaderObject.loadModernSong(applicationContext)
                    loadList()
                    MainMenuActivity.updatePoints(modernSongPoints)
                    resetModern()

                } else {
                    wrongGuessModern()
                }

            } else {
                if (guess!!.text.toString().equals(
                        FileReaderObject.transformToNiceString(
                            FileReaderObject.getClassicSong()
                        )
                    )
                ) {
                    FileReaderObject.addToSongHistoryList(
                        Song(
                            FileReaderObject.getClassicSong(), "Classic"
                        )
                    )
                    startActivity(intentPopUp)
                    clearClassicList()
                    FileReaderObject.loadClassicSong(applicationContext)
                    loadList()
                    MainMenuActivity.updatePoints(classicSongPoints)
                    resetClassic()
                } else {
                    wrongGuessClassic()
                }
            }
        }
        guess!!.setAdapter(FileReaderObject.setAdapter(this))
        loadList()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show()

    }

    private fun loadList() {
        val lyricsList: MutableList<String> = populateList()
        val recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val mAdapter = LyricAdapter(lyricsList)
        recyclerView.adapter = mAdapter
    }


    private fun populateList(): MutableList<String> {
        var list: MutableList<String> = ArrayList()
        val msg = resources.getString(R.string.collect_lyric_message)
        if (MainMenuActivity.getMode()) {
            list = getModernLyrics()
        } else {
            list = getClassicLyrics()
        }

        if (list.size == 0) {
            textViewPoints.visibility = View.INVISIBLE
            list.add(msg)
        } else {
            if (list.size == 1 && list.contains(msg)) {
                textViewPoints.visibility = View.INVISIBLE
                list.remove(msg)
                list.add(msg)
            } else {
                list.remove(msg)
            }
        }
        return list
    }

    companion object LyricHolder {
        private var initialStart = 0
        private var modernSongPoints = 21
        private var classicSongPoints = 21
        private var modernLyricsList: MutableList<String> = ArrayList()
        private var classicLyricsList: MutableList<String> = ArrayList()

        fun resetClassic() {
            classicSongPoints = 21
        }

        fun resetModern() {
            modernSongPoints = 21
        }

        fun additionalLyricModern() {
            modernSongPoints--
        }

        fun additionalLyricClassic() {
            classicSongPoints--
        }

        fun wrongGuessModern() {
            modernSongPoints -= 2
        }

        fun wrongGuessClassic() {
            classicSongPoints -= 2
        }

        fun addModernLyric(lyric: String) {
            modernLyricsList.add(lyric)
        }

        fun addClassicLyric(lyric: String) {
            classicLyricsList.add(lyric)
        }

        fun getModernLyrics(): MutableList<String> {
            return modernLyricsList
        }

        fun getClassicLyrics(): MutableList<String> {
            return classicLyricsList
        }

        fun clearModernList() {
            modernLyricsList.clear()
        }

        fun clearClassicList() {
            classicLyricsList.clear()
        }
    }
}
