package com.example.instagramclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.instagramclone.R
import com.example.instagramclone.adapter.ViewPagerAdapter
import com.example.instagramclone.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * It contains view pager with 5 fragments in MainActivity,
 * and pages can be controlled BottomNavigationView
 */

class MainActivity : BaseActivity(), HomeFragment.HomeListener, UploadFragment.UploadListener {

    var index = 0
    lateinit var homeFragment: HomeFragment
    lateinit var uploadFragment: UploadFragment
    lateinit var viewPager: ViewPager
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    override fun scrollToHome() {
        index = 0
        scrollByIndex(index)
    }

    override fun scrollToUpload() {
        index = 2
        scrollByIndex(index)
    }

    private fun scrollByIndex(index: Int) {
        viewPager.currentItem = index
        bottomNavigationView.menu.getItem(index).isChecked = true
    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPager.currentItem = 0
                R.id.navigation_search -> viewPager.currentItem = 1
                R.id.navigation_upload -> viewPager.currentItem = 2
                R.id.navigation_favorite -> viewPager.currentItem = 3
                R.id.navigation_profile -> viewPager.currentItem = 4
            }
            true
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                index = position
                bottomNavigationView.menu.getItem(index).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        // Home and Upload Fragments are global for communication purpose
        homeFragment = HomeFragment()
        uploadFragment = UploadFragment()
        setupViewPager(viewPager)

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(homeFragment)
        adapter.addFragment(SearchFragment())
        adapter.addFragment(uploadFragment)
        adapter.addFragment(FavoriteFragment())
        adapter.addFragment(ProfileFragment())
        viewPager.adapter = adapter
    }
}