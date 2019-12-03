package com.example.guessthesong

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_lyrics.*

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
        val currentSongs = FileReader.getModernSongs()
        val songs: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, currentSongs)
        guess.setAdapter(songs)
    }


}
