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

    private var songHistoryList: MutableList<Song> = ArrayList()


    /**
     * Returns the list which holds the songs the users have already gone through.
     * @return Returns the list holding the song history
     */
    fun getSongHistoryList(): MutableList<Song> {
        return songHistoryList
    }

    /**
     * Adds song to the song history list.
     * @param addition Song to be added
     */
    fun addToSongHistoryList(addition: Song) {
        songHistoryList.add(addition)
    }

    /**
     * Transforms the file titles to nice and readable strings.
     * @param list list whose entries strings will be transformed
     */
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

    /**
     * Transforms a file name string into a readable string.
     * @param string String to be transformed.
     */
    fun transformToNiceString(string: String): String {
        val arrayOf = string.split("\\(|\\)".toRegex())
        val artistName = arrayOf[0].replace('_', ' ').capitalizeWords()
        val songName = arrayOf[1].replace('_', ' ').capitalizeWords()
        formattedString = songName + " by " + artistName
        return formattedString
    }

    /**
     * Mehtod which capitalizes all words in a string.
     * Source: StackOverflow
     */
    fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

    /**
     * Returns the full text of a song.
     * @param context In which context of the application to call the method.
     * @param path The path to the file which is opened.
     */
    fun getSongText(context: Context, path: String): String {
        return context.assets.open(path).bufferedReader().readText()
    }

    /**
     * Selects and loads a random clasic song from the list. It is then removed so its not selected again.
     * @param context Context from which the method is called.
     */
    fun loadClassicSong(context: Context) {
        classicsContentSearch = context.assets.list(classics)!!.toMutableList()

        classicsContent = context.assets.list(classics)!!.toMutableList()

        classicSongName = classicsContent.random()

        classicSong =
            context.assets.open(classics + "/" + classicSongName)
                .bufferedReader().readLines().toMutableList()

        classicsContent.remove(classicSongName)

    }

    /**
     * Selects and loads a random modern song from the list. It is then removed so its not selected again.
     * @param context Context from which the method is called.
     */
    fun loadModernSong(context: Context) {

        modernContentSearch = context.assets.list(modern)!!.toMutableList()

        modernContent = context.assets.list(modern)!!.toMutableList()

        currentSongName = modernContent.random()

        currentSong =
            context.assets.open(modern + "/" + currentSongName)
                .bufferedReader().readLines().toMutableList()

        modernContent.remove(currentSongName)
    }

    /**
     * Select a random line from the text of the selected classic song and returns it.
     * @return A random line from the song.
     */
    fun nextLineClassic(): String {
        val randomLyricLine = classicSong.random()
        classicSong.remove(randomLyricLine)
        return randomLyricLine
    }
    /**
     * Select a random line from the text of the selected modern song and returns it.
     * @return A random line from the song.
     */
    fun nextLineCurrent(): String {
        val randomLyricLine = currentSong.random()
        currentSong.remove(randomLyricLine)
        return randomLyricLine
    }

    /**
     * Returns the full contents of the modern songs list for search.
     * @return Full list of modern songs.
     */
    fun getModernSongsSearch(): MutableList<String> {
        return modernContentSearch
    }

    /**
     * Returns the full contents of the classic songs list for search.
     * @return Full list of modern songs.
     */
    fun getClassicSongsSearch(): MutableList<String> {
        return classicsContentSearch
    }

    /**
     * Gets the name of the currently selected classic song.
     * @return Currently selected classic song.
     */
    fun getClassicSong(): String {
        return classicSongName
    }

    /**
     * Gets the name of the currently selected modern song.
     * @return Currently selected modern song.
     */
    fun getModernSong(): String {
        return currentSongName
    }

    /**
     * Sets the adapter for the autocomplete guessing field based on the play mode selected.
     * @param context Context from which the method is called.
     * @return The ready adapter for the AutoCompleteTextView based on the play mode selected.
     */
    fun setAdapter(context: Context): ArrayAdapter<String> {
        if (MainMenuActivity.getMode()) {
            var songs: ArrayAdapter<String> =
                ArrayAdapter(
                    context,
                    R.layout.simple_list_item_1,
                    transformListToNiceString(getModernSongsSearch())
                )
            return songs
        } else {
            var songs: ArrayAdapter<String> =
                ArrayAdapter(
                    context,
                    R.layout.simple_list_item_1,
                    (transformListToNiceString(getClassicSongsSearch()))
                )
            return songs
        }
    }
}