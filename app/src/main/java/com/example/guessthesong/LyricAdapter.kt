package com.example.guessthesong

import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class LyricAdapter(private val lyricsModelArrayList: MutableList<String>) :
    RecyclerView.Adapter<LyricAdapter.ViewHolder>() {

    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(pos: Int, aView: View)
    }

   inner class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout), View.OnClickListener {

       override fun onClick(v: View) {
           mClickListener.onClick(adapterPosition, v)
       }

       var lyricTextView: TextView

        init {
            lyricTextView = layout.findViewById(R.id.row_layout) as TextView

            lyricTextView.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.row_layout, parent, false)
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