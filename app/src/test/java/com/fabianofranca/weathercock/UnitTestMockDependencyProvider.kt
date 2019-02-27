package com.fabianofranca.weathercock

import android.app.Application
import android.arch.lifecycle.ViewModel
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.infrastructure.api.WeatherApi
import com.fabianofranca.weathercock.infrastructure.json.DateAdapter
import com.fabianofranca.weathercock.providers.room.Database
import com.fabianofranca.weathercock.views.ViewModelFactory
import com.squareup.moshi.Moshi
import com.squareup.otto.Bus
import kotlinx.coroutines.Dispatchers
import okhttp3.mockwebserver.MockWebServer
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.robolectric.RuntimeEnvironment
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type

class UnitTestMockDependencyProvider : DependencyProvider {

    override fun application(): Application = RuntimeEnvironment.application

    override fun viewModelFactory() = ViewModelFactory()

    override fun viewModels(): Map<Type, ViewModel> = mapOf()

    val mockWebServer = MockWebServer()

    private val api: WeatherApi

    private var units = Units.METRIC

    init {

        DependencyProvider.Current = this

        mockWebServer.start()

        api = spy(
            Retrofit.Builder()
                .baseUrl(mockWebServer.url("/").toString())
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(DateAdapter()).build()))
                .build()
                .create(WeatherApi::class.java)
        )
    }

    override fun bus(): Bus = mock(Bus::class.java)

    override fun apiKey() = "TestApiKey"

    override fun api() = api

    override fun injectUnits(units: Units) {
        this.units = units
    }

    override fun units() = units

    override fun uiDispatcher() = Dispatchers.Default

    override fun ioDispatcher() = Dispatchers.Default

    override fun connected() = true

    override fun database() = mock(Database::class.java)
}