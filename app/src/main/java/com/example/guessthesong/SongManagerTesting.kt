package com.example.guessthesong

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.nio.file.*
import java.util.*


class SongManagerTesting  : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_manager_testing)
        val text = assets.open("Classic/nirvana(smells_like_teen_spirit).txt").bufferedReader().readText()
        println(text)

        val directory = File("Classic")
        println(directory)
        val songNames = directory.listFiles()
        println(songNames)
    }
}
