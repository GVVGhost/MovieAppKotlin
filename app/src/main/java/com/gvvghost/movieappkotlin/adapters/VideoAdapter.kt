package com.gvvghost.movieappkotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gvvghost.movieappkotlin.R
import com.gvvghost.movieappkotlin.pojo.MovieVideo

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    var onVideoClickListener: OnVideoClickListener? = null
    var videos: List<MovieVideo> = ArrayList()
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.textViewVideoName.text = video.name
        holder.itemView.setOnClickListener { onVideoClickListener?.onVideoClick(video) }
    }

    override fun getItemCount(): Int = videos.size

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewVideoName: TextView = itemView.findViewById(R.id.textViewVideoName)
    }

    interface OnVideoClickListener {
        fun onVideoClick(movieVideo: MovieVideo)
    }
}
