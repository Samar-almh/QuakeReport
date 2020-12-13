package com.samar.quakereport


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.samar.quakereport.RecentEarthquakeFragment

class RecentEarthquakeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_earthquake)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, RecentEarthquakeFragment.newInstance())
                .commit()
        }
    }


}