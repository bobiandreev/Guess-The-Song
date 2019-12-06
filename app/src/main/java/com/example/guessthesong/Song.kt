package com.example.guessthesong

import android.content.Context
import android.icu.text.CaseMap

class Song (title: String, time : String){
    private var songTitle = title
    private var songTime = time

    fun getTitle() : String{
        return songTitle
    }

    fun getText(context: Context) : String{
        return FileReaderObject.getSongText(context, (songTime + "/" + songTitle))
    }
}