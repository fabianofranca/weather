package com.fabianofranca.weathercock.infrastructure

import android.app.Application
import android.arch.lifecycle.ViewModel
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.infrastructure.api.WeatherApi
import com.fabianofranca.weathercock.views.ViewModelFactory
import com.squareup.otto.Bus
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext

interface DependencyProvider {

    fun application(): Application

    fun apiKey(): String

    fun api(): WeatherApi

    fun viewModelFactory(): ViewModelFactory

    fun viewModels(): Map<Type, ViewModel>

    fun bus(): Bus

    fun injectUnits(units: Units)

    fun units(): Units

    fun injectLocation(location: Location)

    fun location(): Location

    fun uiDispatcher(): CoroutineContext

    fun ioDispatcher(): CoroutineContext

    companion object {
        lateinit var Current: DependencyProvider
    }
}