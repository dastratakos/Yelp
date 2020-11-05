package edu.stanford.dstratak.yelp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_business_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "lR-eskg2rr8aNV_HMW5N3v8XSSME8IwuPSAPQtGklX-jx4eNkx36OxdOdOiSxbCNrzDv" +
        "HMnXam73Hfqhizx5bwLoL-j1pIoiAW5wrZAr9m1CQfY2Ngxv33JYB4SiX3Yx"
private const val TAG = "BusinessDetailActivity"
class BusinessDetailActivity : AppCompatActivity() {

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_details)

        id = intent.getSerializableExtra(BUSINESS_ID) as String

        setSupportActionBar(findViewById(R.id.toolbar_details))
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        val photos = mutableListOf<String>()

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
                    photos.addAll(body.photos)
                    bind(body)
                }

                override fun onFailure(call: Call<YelpBusinessDetail>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                }
            })
    }

    fun bind(body: YelpBusinessDetail) {
        Glide.with(this@BusinessDetailActivity).load(body.imageUrl).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )).into(imageViewBusinessDetail)

        tvName.text = body.name
        ratingBar.rating = body.rating.toFloat()
        tvNumReviews.text = "${body.numReviews} Reviews"
        tvPrice.text = body.price
        tvCategory.text = body.categories.joinToString { c -> c.title }
        tvAddress.text = body.location.address
        tvPhone.text = "Phone: ${body.phone}"
        tvTransactions.text = body.transactions.joinToString { t -> t.capitalize() }
    }
}
