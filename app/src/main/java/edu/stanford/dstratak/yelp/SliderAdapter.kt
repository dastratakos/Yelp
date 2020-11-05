package edu.stanford.dstratak.yelp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_image_slider.view.*


class SliderAdapter(val context: Context, private val photos: List<String>) : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {

    override fun getCount() = photos.size

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        return SliderAdapterVH(
            LayoutInflater.from(context).inflate(R.layout.item_image_slider, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem = photos[position]
        viewHolder.bind(sliderItem)
    }

    inner class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        fun bind(photo: String) {
            Glide.with(context)
                .load(photo)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(20)))
                .into(itemView.imageViewSlider)
        }
    }
}
