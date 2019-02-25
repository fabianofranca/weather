package com.fabianofranca.weathercock

import android.app.Application

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ReleaseDependencyProvider(this)
    }
}