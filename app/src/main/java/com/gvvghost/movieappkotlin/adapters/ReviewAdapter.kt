package com.gvvghost.movieappkotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gvvghost.movieappkotlin.R
import com.gvvghost.movieappkotlin.pojo.AuthorReview
import kotlinx.android.synthetic.main.review_item.view.*

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    var reviews: List<AuthorReview> = ArrayList()
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.tvAuthorName.text = review.author
        holder.tvReview.text = review.content
        val colorId = review.authorDetail?.rating?.run {
            if (this > 7) android.R.color.holo_green_light
            else if (this > 5) android.R.color.holo_orange_light
            else android.R.color.holo_red_light
        } ?: run { android.R.color.darker_gray }

        holder.cvReview.setCardBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                colorId
            )
        )
    }

    override fun getItemCount(): Int = reviews.size

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvReview: CardView = itemView.findViewById(R.id.cardViewReview)
        val tvAuthorName: TextView = itemView.findViewById(R.id.textViewAuthorName)
        val tvReview: TextView = itemView.findViewById(R.id.textViewReview)
    }
}