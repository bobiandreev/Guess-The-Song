package com.example.guessthesong

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


object SongManagerTesting : AppCompatActivity() {

    private const val classics = "Classic"
    private const val modern = "Current"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_song_manager_testing)
        readFiles()

    }
     fun readFiles()  {
        val classicsContent = assets.list(classics)!!.toMutableList()
        val modernContent = assets.list(modern)!!.toMutableList()

        val classicsText = classicsContent!!.random()
        val modernText = modernContent!!.random()
        val classicSong = assets.open(classics + "/" + classicsText).bufferedReader().readLines().toMutableList()
        val currentSong = assets.open(modern + "/" + modernText).bufferedReader().readLines().random()

        classicsContent.remove(classicsText)
        modernContent.remove(modernText)

        val nextLineClass = nextLineClassic((classicSong))
        classicSong.remove(nextLineClass)
        println(nextLineClass)
        println(currentSong)
    }

    fun nextLineClassic(classicSong: MutableList<String>) : String {
        return classicSong.random()

    }
}
