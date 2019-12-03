package com.example.guessthesong

import android.content.Context

object FileReader {
    private val classics = "Classic"
    private val modern = "Current"
    private lateinit var classicsContent: MutableList<String>
    private lateinit var modernContent: MutableList<String>
    private lateinit var currentSong: MutableList<String>
    private lateinit var classicSong: MutableList<String>
    private lateinit var classicSongName: String
    private lateinit var currentSongName: String



    fun loadSongs(context: Context) {
        classicsContent = context.assets.list(classics)!!.toMutableList()
        modernContent = context.assets.list(modern)!!.toMutableList()

        classicSongName = classicsContent.random()
        currentSongName = modernContent.random()
        classicSong =
            context.assets.open(classics + "/" + classicSongName)
                .bufferedReader().readLines().toMutableList()
        currentSong =
            context.assets.open(modern + "/" + currentSongName)
                .bufferedReader().readLines().toMutableList()
        classicsContent.remove(classicSongName)
        modernContent.remove(currentSongName)
    }

    fun nextLineClassic(): String {
        val randomLyricLine = classicSong.random()

        classicSong.remove(randomLyricLine)
        return randomLyricLine
    }

    fun nextLineCurrent(): String {
        val randomLyricLine = currentSong.random()

        currentSong.remove(randomLyricLine)
        return randomLyricLine
    }

    fun getModernSongs(): MutableList<String> {
        return modernContent
    }

    fun getClassicSongs(): MutableList<String> {
        return classicsContent
    }
}