package com.fabianofranca.weather

import android.app.Application
import com.fabianofranca.weather.infrastructure.DependencyProvider

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DependencyProvider.Current = ReleaseDependencyProvider()
    }
}