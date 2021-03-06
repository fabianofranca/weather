package com.fabianofranca.weathercock

import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.Room
import android.content.Context
import android.net.ConnectivityManager
import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.infrastructure.api.WeatherApi
import com.fabianofranca.weathercock.infrastructure.json.DateAdapter
import com.fabianofranca.weathercock.providers.room.Database
import com.fabianofranca.weathercock.views.ViewModelFactory
import com.fabianofranca.weathercock.views.home.HomeViewModel
import com.fabianofranca.weathercock.views.locations.LocationsViewModel
import com.fabianofranca.weathercock.views.weather.WeatherViewModel
import com.squareup.moshi.Moshi
import com.squareup.otto.Bus
import kotlinx.coroutines.Dispatchers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type

class ReleaseDependencyProvider(private val application: WeatherApplication) : DependencyProvider {

    init {
        DependencyProvider.Current = this
    }

    private val httpClient: OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val api: WeatherApi = Retrofit.Builder()
        .baseUrl(HttpUrl.parse("https://api.openweathermap.org/data/2.5/")!!)
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(DateAdapter()).build()))
        .build()
        .create(WeatherApi::class.java)

    private val apiKey = "2cebe458561b8e39965d632395a2852e"

    private val viewModelFactory = ViewModelFactory()

    private val bus = Bus()

    private var units: Units = Units.METRIC

    private val viewModels = mapOf<Type, ViewModel>(
        WeatherViewModel::class.java to WeatherViewModel(application, bus),
        HomeViewModel::class.java to HomeViewModel(bus),
        LocationsViewModel::class.java to LocationsViewModel(application, bus)
    )

    @Volatile
    private var database: Database? = null

    override fun application() = application

    override fun apiKey() = apiKey

    override fun api() = api

    override fun viewModelFactory() = viewModelFactory

    override fun viewModels() = viewModels

    override fun bus() = bus

    override fun injectUnits(units: Units) {
        this.units = units
    }

    override fun units() = units

    override fun uiDispatcher() = Dispatchers.Main

    override fun ioDispatcher() = Dispatchers.IO

    override fun connected(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnected
    }

    override fun database(): Database {
        return database ?: synchronized(this) {
            database ?: buildDatabase(application)
        }
    }

    private fun buildDatabase(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "weather-database")
            .build()
    }
}