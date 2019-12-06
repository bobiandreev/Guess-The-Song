package com.example.guessthesong

import android.content.Context
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
    /**
     * Method called every time the Lyrics activity is called into the foreground used to set it up.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics)
        setSupportActionBar(toolbar)


        //Toast.makeText(this, FileReaderObject.getClassicSong(), Toast.LENGTH_SHORT).show()
        /**
         * Sets the listener for the Skip Song button. This allows a user to skip a song if they can't guess it.
         */
        skipThisSongButton.setOnClickListener { view ->
          skipSong(applicationContext, this)
        }

       updatePoints()

        guess = findViewById(R.id.actv)

        /**
         * Handles clicking on a field in the autocomplete view and subsequently checking if the
         * selected entry is the correct one. It either adds the points to the user's total or subtracts
         * because of the wrong guess. It also loads the next song when this one is correctly guessed
         * and reset the list.
         */
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
                    loadList(applicationContext, this)
                    MainMenuActivity.updatePoints(modernSongPoints)
                    resetModern()

                } else {
                    wrongGuessModern()
                    if (getModernSongPoints() <= 0){
                        skipSong(applicationContext, this)
                    }
                    updatePoints()
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
                    loadList(applicationContext, this)
                    MainMenuActivity.updatePoints(classicSongPoints)
                    resetClassic()
                } else {
                    wrongGuessClassic()
                    if (getClassicSongPoints() <= 0){
                        skipSong(applicationContext, this)
                    }
                    updatePoints()
                }
            }
        }

        guess!!.setAdapter(FileReaderObject.setAdapter(this))
        loadList(applicationContext, this)
    }
    /**
     * Used to update the points after a guess or a new lyric collected.
     */
   private fun updatePoints() {
        val pointsForSong = resources.getString(R.string.points_for_song)
        if (MainMenuActivity.getMode()) {
            textViewPoints.setText(pointsForSong + modernSongPoints)
        } else {
            textViewPoints.setText(pointsForSong + classicSongPoints)
        }
    }



    companion object LyricHolder {
        private var modernSongPoints = 21
        private var classicSongPoints = 3
        private var modernLyricsList: MutableList<String> = ArrayList()
        private var classicLyricsList: MutableList<String> = ArrayList()


        fun getModernSongPoints() : Int{
            return modernSongPoints
        }

        fun getClassicSongPoints() : Int{
            return classicSongPoints
        }

        /**
         * Loads list with the data required and places in the RecyclerView.
         */
        private fun loadList(context: Context, activity: LyricsActivity) {
            val lyricsList: MutableList<String> = populateList(context, activity)
            val recyclerView = activity.findViewById<View>(R.id.recyclerView) as RecyclerView
            val layoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = layoutManager
            val mAdapter = LyricAdapter(lyricsList)
            recyclerView.adapter = mAdapter
        }

        /**
         * Populates the list whose data will be used in the recycler view. Checks if there are entries
         * and displays a message accordingly.
         */
        private fun populateList(context: Context, activity: LyricsActivity): MutableList<String> {
            var list: MutableList<String> = ArrayList()
            val msg = context.resources.getString(R.string.collect_lyric_message)
            if (MainMenuActivity.getMode()) {
                list = getModernLyrics()
            } else {
                list = getClassicLyrics()
            }

            if (list.size == 0) {
                activity.textViewPoints.visibility = View.INVISIBLE
                list.add(msg)
            } else {
                if (list.size == 1 && list.contains(msg)) {
                   activity.textViewPoints.visibility = View.INVISIBLE
                    list.remove(msg)
                    list.add(msg)
                } else {
                    list.remove(msg)
                }
            }
            return list
        }


        fun skipSong(context: Context, activity: LyricsActivity) {
            val message = activity.resources.getString(R.string.fail_message)
            val intent = Intent(context, PopUpActivity::class.java)

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
                context.startActivity(intent)
                clearModernList()
                FileReaderObject.loadModernSong(context)
                loadList(context, activity)
                resetModern()
            } else {
                intent.putExtra(
                    "STRING",
                    message + FileReaderObject.transformToNiceString(FileReaderObject.getClassicSong())
                )
                FileReaderObject.addToSongHistoryList(
                    Song(FileReaderObject.getClassicSong(), "Classic")
                )
                context.startActivity(intent)
                clearClassicList()
                FileReaderObject.loadClassicSong(context)
                loadList(context, activity)
                resetClassic()
            }
        }

        /**
         * Resets the points for the classic song to the full amount.
         */
        fun resetClassic() {
            classicSongPoints = 21
        }

        /**
         * Resets the points for the modern song to the full amount.
         */
        fun resetModern() {
            modernSongPoints = 21
        }

        /**
         * Subtracts a point from the modern score when an additional lyric is added.
         */
        fun additionalLyricModern() {
            modernSongPoints--
        }

        /**
         * Subtracts a point from the classic score when an additional lyric is added.
         */
        fun additionalLyricClassic() {
            classicSongPoints--
        }

        /**
         * Subtracts 2 points from the modern score when an additional lyric is added.
         */
        fun wrongGuessModern() {
            modernSongPoints -= 2
        }

        /**
         * Subtracts 2 points from the classic score when an additional lyric is added.
         */
        fun wrongGuessClassic() {
            classicSongPoints -= 2
        }

        /**
         * Adds a lyric from the modern song to its list to be displayed in the Recycler View.
         * @param lyric Name of the song to be added
         */
        fun addModernLyric(lyric: String) {
            modernLyricsList.add(lyric)
        }

        /**
         * Adds a lyric from the classic song to its list to be displayed in the Recycler View.
         * @param lyric Name of the song to be added
         */
        fun addClassicLyric(lyric: String) {
            classicLyricsList.add(lyric)
        }

        /**
         * Gets the full list of lyrics available for the modern song.
         * @return All of the lyrics available for the modern song.
         */
        fun getModernLyrics(): MutableList<String> {
            return modernLyricsList
        }

        /**
         * Gets the full list of lyrics available for the classic song.
         * @return All of the lyrics available for the classic song.
         */
        fun getClassicLyrics(): MutableList<String> {
            return classicLyricsList
        }

        /**
         * Wipes the list holding the modern lyrics.
         */
        fun clearModernList() {
            modernLyricsList.clear()
        }

        /**
         * Wipes the list holding the classic lyrics.
         */
        fun clearClassicList() {
            classicLyricsList.clear()
        }
    }
}
