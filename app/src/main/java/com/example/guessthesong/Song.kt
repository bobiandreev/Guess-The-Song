package com.example.guessthesong

import android.content.Context

/**
 * A song object. Has a title and time period properties.
 */
class Song (title: String, time : String){
    private var songTitle = title
    private var songTime = time

    /**
     * Returns the songs title.
     * @return The title of the song.
     */
    fun getTitle() : String{
        return songTitle
    }

    /**
     * Returns the full song text.
     * @param context Context from where the method is called.
     */
    fun getText(context: Context) : String{
        return FileReaderObject.getSongText(context, (songTime + "/" + songTitle))
    }
}