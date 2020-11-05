package edu.stanford.dstratak.yelp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class BusinessDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_details)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }
}
