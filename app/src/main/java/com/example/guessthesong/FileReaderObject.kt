package com.example.guessthesong

import android.R
import android.content.Context
import android.widget.ArrayAdapter

object FileReaderObject {
    private val classics = "Classic"
    private val modern = "Current"

    private lateinit var classicsContent: MutableList<String>
    private lateinit var modernContent: MutableList<String>

    private lateinit var currentSong: MutableList<String>
    private lateinit var classicSong: MutableList<String>

    private lateinit var classicSongName: String
    private lateinit var currentSongName: String

    private lateinit var classicsContentSearch: MutableList<String>
    private lateinit var modernContentSearch: MutableList<String>
    private var formattedList: MutableList<String> = ArrayList()
    private var formattedString = ""

    private var songHistoryList : MutableList<Song> = ArrayList()


    fun getSongHistoryList() : MutableList<Song>{
        return songHistoryList
    }

    fun addToSongHistoryList(addition : Song){
        songHistoryList.add(addition)
    }

    fun transformListToNiceString(list: MutableList<String>): MutableList<String> {
        list.forEach { s ->
            // s.dropLast(4)
            val arrayOf = s.split("\\(|\\)".toRegex())
            val artistName = arrayOf[0].replace('_', ' ').capitalizeWords()
            val songName = arrayOf[1].replace('_', ' ').capitalizeWords()
            val formattedString = songName + " by " + artistName
            formattedList.add(formattedString)
        }
        return formattedList
    }

    fun transformToNiceString(string: String) : String{
        val arrayOf = string.split("\\(|\\)".toRegex())
        val artistName = arrayOf[0].replace('_', ' ').capitalizeWords()
        val songName = arrayOf[1].replace('_', ' ').capitalizeWords()
        formattedString =  songName + " by " + artistName
        return formattedString
    }

    fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

    fun getSongText(context: Context, path : String) : String {
        return context.assets.open(path).bufferedReader().readText()
    }

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

    fun getClassicSong(): String {
        return classicSongName
    }

    fun getModernSong(): String {
        return currentSongName
    }

    fun setAdapter(context: Context): ArrayAdapter<String> {
        if (MainMenuActivity.getMode()) {
            var songs: ArrayAdapter<String> =
                ArrayAdapter(
                    context,
                    R.layout.simple_list_item_1,
                    transformListToNiceString(getModernSongsSearch())
                )
            /*for (song in getModernSongsSearch()) {
                var i = 0
                while (i < songs.count) {
                    if (songs.getItem(i).equals(song)) {
                        songs.remove(song)
                    }
                }
                songs.add(song)
            }*/
            return songs
        } else {
            var songs: ArrayAdapter<String> =
                ArrayAdapter(
                    context,
                    R.layout.simple_list_item_1,
                    (transformListToNiceString(getClassicSongsSearch()))
                )
          /*  for (song in getClassicSongsSearch()) {
                var i = 0
                while (i < songs.count) {
                    if (songs.getItem(i).equals(song)) {
                        songs.remove(song)
                    }
                }
                songs.add(song)
            }*/
            return songs
        }
    }
}