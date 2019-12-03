package com.example.guessthesong

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_lyrics.*
import java.lang.Exception

class LyricsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics)
        setSupportActionBar(toolbar)

        skipThisSongButton.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val guess: AutoCompleteTextView = findViewById(R.id.actv)
        if (MainMenuActivity.getMode()) {
            val songs: ArrayAdapter<String> =
                ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    FileReaderObject.getModernSongs()
                )
            guess.setAdapter(songs)
        } else {
            val songs: ArrayAdapter<String> =
                ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    FileReaderObject.getClassicSongs()
                )
            guess.setAdapter(songs)
        }


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
    }
}
