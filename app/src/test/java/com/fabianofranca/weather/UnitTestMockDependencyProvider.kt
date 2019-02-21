package com.fabianofranca.weather

import com.fabianofranca.weather.entities.Units
import com.fabianofranca.weather.infrastructure.DependencyProvider
import com.fabianofranca.weather.infrastructure.api.WeatherApi
import com.fabianofranca.weather.infrastructure.json.DateAdapter
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class UnitTestMockDependencyProvider : DependencyProvider {

    val mockWebServer = MockWebServer()

    private val api: WeatherApi

    private var units = Units.METRIC

    init {
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(DateAdapter()).build()))
            .build()
            .create(WeatherApi::class.java)
    }

    override fun apiKey() = ""

    override fun api() = api

    override fun injectUnits(units: Units) {
        this.units = units
    }

    override fun units() = units
}