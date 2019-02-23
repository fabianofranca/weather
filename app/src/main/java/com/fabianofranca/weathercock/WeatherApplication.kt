package com.fabianofranca.weathercock

import android.app.Application
import com.fabianofranca.weathercock.infrastructure.DependencyProvider

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DependencyProvider.Current = ReleaseDependencyProvider()
    }
}