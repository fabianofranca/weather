package com.fabianofranca.weather.infrastructure

import com.fabianofranca.weather.entities.Units
import com.fabianofranca.weather.infrastructure.api.WeatherApi

interface DependencyProvider {

    fun apiKey(): String

    fun api(): WeatherApi

    fun injectUnits(units: Units)

    fun units(): Units

    companion object {
        lateinit var Current: DependencyProvider
    }
}