package com.example.guessthesong

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File
import java.nio.file.*
import java.util.*


class SongManagerTesting : AppCompatActivity() {

    private val classics = "Classic"
    private val modern = "Current"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_manager_testing)
        val classicsContent = assets.list(classics)
        val modernContent = assets.list(modern)
        val classicsText = classicsContent!!.random()
        val modernText = modernContent!!.random()
        val classicSong = assets.open(classics + "/" + classicsText).bufferedReader().readText()
        val currentSong = assets.open(modern + "/" + modernText).bufferedReader().readText()
        println(classicSong)
        println(currentSong)
    }

}
