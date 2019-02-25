package com.fabianofranca.weathercock.views.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.views.launch.LaunchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(R.id.container, LaunchFragment())
        transaction.commit()
    }
}
