package edu.stanford.dstratak.yelp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_restaurant_overview.*
import kotlinx.android.synthetic.main.fragment_restaurant_reviews.*
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "lR-eskg2rr8aNV_HMW5N3v8XSSME8IwuPSAPQtGklX-jx4eNkx36OxdOdOiSxbCNrzDv" +
        "HMnXam73Hfqhizx5bwLoL-j1pIoiAW5wrZAr9m1CQfY2Ngxv33JYB4SiX3Yx"
private const val TAG = "RestaurantDetailActivity"
class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var id: String
    lateinit var photosAdapter: SliderAdapter
    val photos = mutableListOf<String>()
    val reviews = mutableListOf<YelpReview>()

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        id = intent.getSerializableExtra(RESTAURANT_ID) as String

        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        var pagerAdapter = MyFragmentPagerAdapter(supportFragmentManager, this)
        viewPager.adapter = pagerAdapter

        pagerAdapter.addFragment(OverviewFragment(), "Overview")
        pagerAdapter.addFragment(ReviewsFragment(), "Reviews")
        pagerAdapter.notifyDataSetChanged()

        val tabLayout = findViewById<View>(R.id.sliding_tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)

        // *************************** Yelp API calls ***************************
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        val yelpService = retrofit.create(YelpService::class.java)
        yelpService.getDetails("Bearer $API_KEY", id)
            .enqueue(object : Callback<YelpRestaurantDetail> {
                override fun onResponse(
                        call: Call<YelpRestaurantDetail>,
                        response: Response<YelpRestaurantDetail>
                ) {
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                        return
                    }
                    bindDetails(body)
                    photos.addAll(body.photos.drop(1))
                    photosAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<YelpRestaurantDetail>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                }
            })

        yelpService.getReviews("Bearer $API_KEY", id)
            .enqueue(object : Callback<YelpReviews> {
                override fun onResponse(
                        call: Call<YelpReviews>,
                        response: Response<YelpReviews>
                ) {
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                        return
                    }
                    bindReviews()
                    reviews.addAll(body.reviews)
                    pagerAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<YelpReviews>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                }
            })

    }

    @ExperimentalStdlibApi
    fun bindDetails(body: YelpRestaurantDetail) {
        Glide.with(this@RestaurantDetailActivity)
            .load(body.imageUrl)
            .apply(RequestOptions().transform(CenterCrop()))
            .into(imageViewRestaurantDetail)

        photosAdapter = SliderAdapter(this, photos)
        imageSlider.setSliderAdapter(photosAdapter)

        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
        imageSlider.indicatorSelectedColor = Color.WHITE
        imageSlider.indicatorUnselectedColor = Color.GRAY
        imageSlider.scrollTimeInSec = 4
        imageSlider.startAutoCycle()

        tvName.text = body.name
        ratingBar.rating = body.rating.toFloat()
        tvNumReviews.text = "${body.numReviews} Reviews"
        tvPrice.text = body.price
        tvCategory.text = body.categories.joinToString { c -> c.title }
        tvAddress.text = body.location.address
        tvPhone.text = "Phone: ${body.phone}"
        tvTransactions.text = body.transactions.joinToString { t -> t.capitalize(Locale.getDefault()) }
    }

    fun bindReviews() {
        rvReviews.adapter = ReviewAdapter(this, reviews)
        rvReviews.layoutManager = LinearLayoutManager(this)
        rvReviews.addItemDecoration(
            DividerItemDecoration(
                    rvReviews.context,
                    DividerItemDecoration.VERTICAL
            )
        )
    }
}
