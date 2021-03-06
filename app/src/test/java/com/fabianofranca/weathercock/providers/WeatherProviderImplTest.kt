package com.fabianofranca.weathercock.providers

import com.fabianofranca.weathercock.UnitTestMockDependencyProvider
import com.fabianofranca.weathercock.entities.Clear
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.extensions.fileContent
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.verify
import java.util.*

class WeatherProviderImplTest {

    private val dependencyProvider =
        UnitTestMockDependencyProvider()

    private val weatherPath = "weather.json"

    private val forecastPath = "forecast.json"

    private val uviPath = "uvi.json"

    init {
        DependencyProvider.Current = dependencyProvider
    }

    @Test
    fun weather_shouldReturnWeather() {

        dependencyProvider.mockWebServer.enqueue(MockResponse().setBody(weatherPath.fileContent()))

        val provider = WeatherProviderImpl()

        val weather = provider.current(Location.SILVERSTONE, Units.METRIC)

        verify(dependencyProvider.api()).weather("Silverstone, UK", "TestApiKey", "metric")

        assertTrue(weather.condition is Clear)
        assertEquals(weather.temperature, 9)
    }

    @Test(expected = Exception::class)
    fun weather_shouldThrowException() {

        dependencyProvider.mockWebServer.enqueue(MockResponse().setResponseCode(400))

        WeatherProviderImpl().current(Location.SILVERSTONE, Units.METRIC)
    }

    @Test
    fun forecast_shouldReturnWeather() {

        dependencyProvider.mockWebServer.enqueue(MockResponse().setBody(forecastPath.fileContent()))

        val provider = WeatherProviderImpl()

        val weather = provider.fiveDay(Location.SILVERSTONE, Units.METRIC)

        verify(dependencyProvider.api()).forecast("Silverstone, UK", "TestApiKey", "metric")

        assertEquals(5, weather.size)

        val hours = arrayOf(12, 12, 12, 12, 12).toIntArray()
        val minutesOurSeconds = arrayOf(0, 0, 0, 0, 0).toIntArray()

        assertArrayEquals(hours, weather.map {
            Calendar.getInstance().apply { time = it.date }.get(Calendar.HOUR_OF_DAY)
        }.toIntArray())

        assertArrayEquals(minutesOurSeconds, weather.map {
            Calendar.getInstance().apply { time = it.date }.get(Calendar.MINUTE)
        }.toIntArray())

        assertArrayEquals(minutesOurSeconds, weather.map {
            Calendar.getInstance().apply { time = it.date }.get(Calendar.SECOND)
        }.toIntArray())
    }

    @Test(expected = Exception::class)
    fun forecast_shouldThrowException() {

        dependencyProvider.mockWebServer.enqueue(MockResponse().setResponseCode(400))

        WeatherProviderImpl().fiveDay(Location.SILVERSTONE, Units.METRIC)
    }

    @Test
    fun uvi_shouldReturnWeather() {

        dependencyProvider.mockWebServer.enqueue(MockResponse().setBody(uviPath.fileContent()))

        val provider = WeatherProviderImpl()

        val uvi = provider.uvi(0.0, 0.0)

        verify(dependencyProvider.api()).uvi("", 0.0, 0.0)

        assertEquals(10.16F, uvi.value)
    }

    @Test(expected = Exception::class)
    fun uvi_shouldThrowException() {

        dependencyProvider.mockWebServer.enqueue(MockResponse().setResponseCode(400))

        WeatherProviderImpl().uvi(0.0, 0.0)
    }

}