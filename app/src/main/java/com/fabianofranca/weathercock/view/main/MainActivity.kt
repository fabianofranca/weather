package com.fabianofranca.weathercock.view.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fabianofranca.weathercock.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
