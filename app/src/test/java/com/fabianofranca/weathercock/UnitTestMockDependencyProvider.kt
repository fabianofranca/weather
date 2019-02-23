package com.fabianofranca.weathercock

import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.infrastructure.api.WeatherApi
import com.fabianofranca.weathercock.infrastructure.json.DateAdapter
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