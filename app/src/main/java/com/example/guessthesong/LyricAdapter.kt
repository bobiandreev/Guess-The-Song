package com.example.guessthesong

import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class LyricAdapter(private val lyricsModelArrayList: MutableList<String>) :
    RecyclerView.Adapter<LyricAdapter.ViewHolder>() {
    class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout) {
        var lyricTextView: TextView

        init {
            lyricTextView = layout.findViewById(R.id.lyricLine) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.lyric_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lyricsModelArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = lyricsModelArrayList[position]
        holder.lyricTextView.setText(info)
    }
}