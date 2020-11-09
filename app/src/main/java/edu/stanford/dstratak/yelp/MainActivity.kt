package edu.stanford.dstratak.yelp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BUSINESS_ID = "BUSINESS_ID"
private const val TAG = "MainActivity"
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "lR-eskg2rr8aNV_HMW5N3v8XSSME8IwuPSAPQtGklX-jx4eNkx36OxdOdOiSxbCNrzDv" +
        "HMnXam73Hfqhizx5bwLoL-j1pIoiAW5wrZAr9m1CQfY2Ngxv33JYB4SiX3Yx"
class MainActivity : AppCompatActivity() {

    private lateinit var adapter : RestaurantsAdapter

    private lateinit var restaurants : MutableList<YelpRestaurant>
    private lateinit var retrofit : Retrofit
    private lateinit var yelpService : YelpService

    private var searchQuery = "Avocado Toast"
    private var searchLocation = "New York"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        restaurants = mutableListOf<YelpRestaurant>()
        retrofit =
                Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                        .build()
        yelpService = retrofit.create(YelpService::class.java)

        updateSearch()

        rvRestaurants.layoutManager = LinearLayoutManager(this)
        adapter = RestaurantsAdapter(this, restaurants, object : RestaurantsAdapter.OnClickListener {
            override fun onItemClick(position: Int) {
                Log.i(TAG, "onItemClick $position")
                // When the user taps on a view in RV, navigate to new activity
                val intent = Intent(this@MainActivity, BusinessDetailActivity::class.java)
                intent.putExtra(BUSINESS_ID, restaurants[position].id)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
        rvRestaurants.adapter = adapter
        rvRestaurants.addItemDecoration(
                DividerItemDecoration(
                        rvRestaurants.context,
                        DividerItemDecoration.VERTICAL
                )
        )

    }

    private fun updateSearch() {
        yelpService.searchRestaurants("Bearer $API_KEY", searchQuery, searchLocation)
            .enqueue(object : Callback<YelpSearchResult> {
                override fun onResponse(
                        call: Call<YelpSearchResult>,
                        response: Response<YelpSearchResult>
                ) {
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                        return
                    }
                    restaurants.clear()
                    restaurants.addAll(body.restaurants)
                    Log.i(TAG, "$restaurants")
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.i(TAG, "onCreateOptionsMenu")
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // perform query here
                Log.i(TAG, "onQueryTextSubmit: $query")
                if (query != null) {
                    if (query.contains(" in ")) {
                        val parts = query.split(" in ")
                        searchQuery = parts[0]
                        searchLocation = parts[1]
                    }
                    updateSearch()
                } else {
                    Log.i(TAG, "invalid search")
                }
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}