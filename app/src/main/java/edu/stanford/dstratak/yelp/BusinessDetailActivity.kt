package edu.stanford.dstratak.yelp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_business_details.*
import kotlinx.android.synthetic.main.tab_overview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "lR-eskg2rr8aNV_HMW5N3v8XSSME8IwuPSAPQtGklX-jx4eNkx36OxdOdOiSxbCNrzDv" +
        "HMnXam73Hfqhizx5bwLoL-j1pIoiAW5wrZAr9m1CQfY2Ngxv33JYB4SiX3Yx"
private const val TAG = "BusinessDetailActivity"
class BusinessDetailActivity : AppCompatActivity() {

    private lateinit var id: String

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_details)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        id = intent.getSerializableExtra(BUSINESS_ID) as String

        setSupportActionBar(findViewById(R.id.toolbar_details))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val photos = mutableListOf<String>()

        val adapter = SliderAdapter(this, photos)
        imageSlider.setSliderAdapter(adapter)

        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
        imageSlider.indicatorSelectedColor = Color.WHITE
        imageSlider.indicatorUnselectedColor = Color.GRAY
        imageSlider.scrollTimeInSec = 4
        imageSlider.startAutoCycle()

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        val yelpService = retrofit.create(YelpService::class.java)
        yelpService.getDetails("Bearer $API_KEY", id)
            .enqueue(object : Callback<YelpBusinessDetail> {
                override fun onResponse(
                    call: Call<YelpBusinessDetail>,
                    response: Response<YelpBusinessDetail>
                ) {
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                        return
                    }
                    bind(body)
                    photos.addAll(body.photos.drop(1))
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<YelpBusinessDetail>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                }
            })
    }

    @ExperimentalStdlibApi
    fun bind(body: YelpBusinessDetail) {
        Glide.with(this@BusinessDetailActivity)
            .load(body.imageUrl)
            .apply(RequestOptions().transform(CenterCrop()))
            .into(imageViewBusinessDetail)

        tvName.text = body.name
        ratingBar.rating = body.rating.toFloat()
        tvNumReviews.text = "${body.numReviews} Reviews"
        tvPrice.text = body.price
        tvCategory.text = body.categories.joinToString { c -> c.title }
        tvAddress.text = body.location.address
        tvPhone.text = "Phone: ${body.phone}"
        tvTransactions.text = body.transactions.joinToString { t -> t.capitalize(Locale.getDefault()) }
    }
}
