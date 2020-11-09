package edu.stanford.dstratak.yelp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

private const val TAG = "MyFragmentPagerAdapter"
class MyFragmentPagerAdapter(fragmentManager: FragmentManager, context: Context) :
        FragmentStatePagerAdapter(fragmentManager) {

    private val tabs = mutableListOf<Fragment>()
    private val tabTitles = mutableListOf<String>()
    private val context: Context = context

    override fun getCount(): Int = tabTitles.size

    override fun getItem(position: Int): Fragment = tabs[position]

    override fun getPageTitle(position: Int): CharSequence? = tabTitles[position]

    fun addFragment(fragment: Fragment, title: String) {
        tabs.add(fragment)
        tabTitles.add(title)
    }
}