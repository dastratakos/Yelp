package edu.stanford.dstratak.yelp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_review.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "RestaurantsAdapter"
class ReviewAdapter(val context: Context,
                    private val reviews: List<YelpReview>)
    : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_review, parent, false))
    }

    override fun getItemCount() = reviews.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(review: YelpReview) {
            itemView.tvReviewName.text = review.user.name
            itemView.rbReview.rating = review.rating.toFloat()

            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var date: Date = Date()
            try {
                date = format.parse(review.timestamp)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val dateFormat = SimpleDateFormat("EEE, M/d/yy 'at' h:mm a")
            var dateTime = review.timestamp
            try {
                dateTime = dateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            itemView.tvReviewDate.text = dateTime
            itemView.tvReviewText.text = review.text
        }
    }
}
