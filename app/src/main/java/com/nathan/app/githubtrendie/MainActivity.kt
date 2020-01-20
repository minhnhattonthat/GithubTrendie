package com.nathan.app.githubtrendie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nathan.app.githubtrendie.ui.trending.TrendingFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TrendingFragment.newInstance())
                .commitNow()
        }
    }

}
