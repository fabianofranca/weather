package com.fabianofranca.weathercock

import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.infrastructure.api.WeatherApi
import com.fabianofranca.weathercock.infrastructure.json.DateAdapter
import com.squareup.moshi.Moshi
import com.squareup.otto.Bus
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ReleaseDependencyProvider : DependencyProvider {

    private var units: Units = Units.METRIC

    override fun bus(): Bus = bus

    override fun injectUnits(units: Units) {
        this.units = units
    }

    override fun units() = units

    override fun apiKey() = "2cebe458561b8e39965d632395a2852e"

    override fun api() = api

    private companion object {

        val httpClient: OkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val api: WeatherApi = Retrofit.Builder()
            .baseUrl(HttpUrl.parse("https://api.openweathermap.org/data/2.5/")!!)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(DateAdapter()).build()))
            .build()
            .create(WeatherApi::class.java)

        val bus: Bus = Bus()
    }
}