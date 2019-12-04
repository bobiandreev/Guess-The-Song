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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_lyrics.*


class LyricsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics)
        setSupportActionBar(toolbar)

        skipThisSongButton.setOnClickListener { view ->
            val message = "This song was \n"
            val intent = Intent(applicationContext, PopUpActivity::class.java)

            if (MainMenuActivity.getMode()) {
//                Snackbar.make(view, "This was: " + FileReaderObject.getModernSong(), Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
                intent.putExtra("STRING", message + FileReaderObject.getModernSong())
                startActivity(intent)
                clearModernList()
                FileReaderObject.loadModernSong(applicationContext)
                loadList()
            } else {
//                Snackbar.make(view, "This was: " + FileReaderObject.getClassicSong(), Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
                intent.putExtra("STRING", message + FileReaderObject.getClassicSong())
                startActivity(intent)
                clearClassicList()
                FileReaderObject.loadClassicSong(applicationContext)
                loadList()
            }
        }
        val guess: AutoCompleteTextView = findViewById(R.id.actv)

        if (MainMenuActivity.getMode()) {
            val songs: ArrayAdapter<String> =
                ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    FileReaderObject.getModernSongsSearch()
                )
            guess.setAdapter(songs)
        } else {
            val songs: ArrayAdapter<String> =
                ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    FileReaderObject.getClassicSongsSearch()
                )
            println(FileReaderObject.getClassicSong())
            //FileReaderObject.getClassicSongsSearch().forEach { println(it) }
            guess.setAdapter(songs)
        }

        guess.setOnItemClickListener { parent, arg1, pos, id ->

            if (MainMenuActivity.getMode()) {

                if (guess.text.toString().equals(FileReaderObject.getModernSong())) {
                    val toastNice = Toast.makeText(this, "Nice", Toast.LENGTH_SHORT).show()
                    toastNice
                } else {
                    val toastNotNice = Toast.makeText(this, "Not Nice", Toast.LENGTH_SHORT).show()
                    toastNotNice
                }

            } else {

                if (guess.text.toString().equals(FileReaderObject.getClassicSong())) {
                    val toastNice = Toast.makeText(this, "Nice", Toast.LENGTH_SHORT).show()
                    toastNice
                } else {
                    val toastNotNice = Toast.makeText(this, "Not Nice", Toast.LENGTH_SHORT).show()
                    toastNotNice
                }
            }
        }
        loadList()
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
        if (MainMenuActivity.getMode()) {
            list = getModernLyrics()
        } else {
            list = getClassicLyrics()
        }
        val msg = "Collect a lyric."
        if (list.size == 0) {

            list.add(msg)
        } else {
            try {
                list.remove(msg)
            } catch (e: Exception) {
                println("List is empty")
            }
        }
        return list
    }

    companion object LyricHolder {
        private var modernLyricsList: MutableList<String> = ArrayList()
        private var classicLyricsList: MutableList<String> = ArrayList()

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
