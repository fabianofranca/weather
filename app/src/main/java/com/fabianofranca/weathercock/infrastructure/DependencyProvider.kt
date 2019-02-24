package com.fabianofranca.weathercock.infrastructure

import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.infrastructure.api.WeatherApi
import com.squareup.otto.Bus

interface DependencyProvider {

    fun apiKey(): String

    fun api(): WeatherApi

    fun injectUnits(units: Units)

    fun units(): Units

    fun bus(): Bus

    companion object {
        lateinit var Current: DependencyProvider
    }
}