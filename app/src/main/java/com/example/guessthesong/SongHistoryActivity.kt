package com.example.guessthesong

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SongHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_history)
        loadList()
        val lyricsList: MutableList<String> = populateList()
        val recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val mAdapter = LyricAdapter(lyricsList)

        mAdapter.setOnItemClickListener(object : LyricAdapter.ClickListener {
            override fun onClick(pos: Int, aView: View) {
                var intent = Intent(aView.context, TextViewActivity::class.java)
                intent.putExtra(
                    "STRING",
                    FileReaderObject.getSongHistoryList()[pos].getText(applicationContext)
                )
                intent.putExtra("TITLE", FileReaderObject.getSongHistoryList()[pos].getTitle())
                startActivity(intent)
            }
        })
        recyclerView.adapter = mAdapter

    }

    private fun loadList() {

    }

    private fun populateList(): MutableList<String> {
        var list: MutableList<String> = ArrayList()
        val msg = resources.getString(R.string.msg_song_history_list)
        FileReaderObject.getSongHistoryList().forEach { song: Song ->
            list.add(FileReaderObject.transformToNiceString(song.getTitle()))
        }

        if (list.size == 0) {

            list.add(msg)
        } else {
            if (list.size == 1 && list.contains(msg)) {
                list.remove(msg)
                list.add(msg)
            } else {
                list.remove(msg)
            }
        }
        return list
    }
}
