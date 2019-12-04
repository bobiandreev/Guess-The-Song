package com.example.guessthesong

import android.content.Context

object FileReaderObject {
    private val classics = "Classic"
    private val modern = "Current"

    private lateinit var classicsContent: MutableList<String>
    private lateinit var modernContent: MutableList<String>

    private lateinit var currentSong: MutableList<String>
    private lateinit var classicSong: MutableList<String>

    private lateinit var classicSongName: String
    private lateinit var currentSongName: String

    private lateinit var classicsContentSearch : MutableList<String>
    private lateinit var modernContentSearch : MutableList<String>

    fun loadClassicSong(context: Context) {
        classicsContentSearch = context.assets.list(classics)!!.toMutableList()

        classicsContent = context.assets.list(classics)!!.toMutableList()

        classicSongName = classicsContent.random()

        classicSong =
            context.assets.open(classics + "/" + classicSongName)
                .bufferedReader().readLines().toMutableList()

        classicsContent.remove(classicSongName)

    }

    fun loadModernSong(context: Context) {

        modernContentSearch = context.assets.list(modern)!!.toMutableList()

        modernContent = context.assets.list(modern)!!.toMutableList()

        currentSongName = modernContent.random()

        currentSong =
            context.assets.open(modern + "/" + currentSongName)
                .bufferedReader().readLines().toMutableList()

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

    fun getModernSongsSearch(): MutableList<String> {
        return modernContentSearch
    }

    fun getClassicSongsSearch(): MutableList<String> {
        return classicsContentSearch
    }

    fun getClassicSong() : String {
        return classicSongName
    }

    fun getModernSong() : String{
        return currentSongName
    }
}