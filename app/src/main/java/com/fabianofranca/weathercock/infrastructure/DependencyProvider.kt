package com.fabianofranca.weathercock.infrastructure

import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.infrastructure.api.WeatherApi

interface DependencyProvider {

    fun apiKey(): String

    fun api(): WeatherApi

    fun injectUnits(units: Units)

    fun units(): Units

    companion object {
        lateinit var Current: DependencyProvider
    }
}