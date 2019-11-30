package com.example.guessthesong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.os.Environment
import android.widget.EditText
import android.view.View
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object SongManager {
    private var countClassic = 0
    private var countCurrent = 0

//    fun iterator(){
//        try {
//            var fileInputStream: FileInputStream? = null
//            fileInputStream = openFileInput("mytextfile.txt")
//            var inputStreamReader = InputStreamReader(fileInputStream)
//            val bufferedReader = BufferedReader(inputStreamReader)
//            val stringBuilder: StringBuilder = StringBuilder()
//            var text: String? = null
//            while ({ text = bufferedReader.readLine(); text }() != null) {
//                stringBuilder.append(text)
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

//    fun iterator() {
//        File("com/example/guessthesong/Classic/bob_dylan(like_a_rolling_stone).txt").forEachLine { println(it) }
//        println(countClassic)
//        Log.d("Count ", countClassic.toString())
//    }
}