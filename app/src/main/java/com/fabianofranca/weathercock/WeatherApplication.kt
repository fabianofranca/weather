package com.fabianofranca.weathercock

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.fabianofranca.weathercock.infrastructure.network.InternetAvailableReceiver
import java.util.*

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ReleaseDependencyProvider(this)

        val internetAvailableReceiver = InternetAvailableReceiver()

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

        registerReceiver(internetAvailableReceiver, filter)

        Locale.setDefault(Locale.US)
    }
}