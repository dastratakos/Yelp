package edu.stanford.dstratak.yelp

import android.app.SearchManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView


class SearchableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(res.xml.searchable, menu)
//        val searchItem: MenuItem = menu.findItem(R.id.searchView)
//        val searchView = searchItem.getActionView() as SearchView
//        searchView.queryHint = "Search People"
//        searchView.setOnQueryTextListener(this)
//        searchView.isIconified = false
//        return true
//    }
}