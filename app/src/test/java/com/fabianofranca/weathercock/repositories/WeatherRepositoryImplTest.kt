package com.fabianofranca.weathercock.repositories

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.fabianofranca.weathercock.UnitTestMockDependencyProvider
import com.fabianofranca.weathercock.entities.*
import com.fabianofranca.weathercock.extensions.verify
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.providers.WeatherProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryImplTest {

    @Rule
    @JvmField
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    lateinit var provider: WeatherProvider

    private val weatherCurrent = Weather(Clear, 0, Calendar.getInstance().time)
    private val weatherDay = Weather(Rain, 0, Calendar.getInstance().time)

    private val location = Location.SAO_PAULO
    private val units = Units.METRIC

    init {
        UnitTestMockDependencyProvider()

        DependencyProvider.Current.injectUnits(units)
        DependencyProvider.Current.injectLocation(location)
    }

    @Test
    fun weather_shouldWork() {

        `when`(provider.current(location, units)).thenReturn(weatherCurrent)
        `when`(provider.fiveDay(location, units)).thenReturn(listOf(weatherDay))

        val repository = WeatherRepositoryImpl(provider)

        repository.weather().verify {
            assertEquals(weatherCurrent, it)
            assertEquals(weatherDay, it?.fiveDays?.first())
        }

        verify(provider).current(location, units)
        verify(provider).fiveDay(location, units)
    }

    @Test(expected = Exception::class)
    fun weather_shouldThrowExceptionWhenCurrentFail() {
        `when`(provider.current(location, units)).thenThrow(Exception())
        `when`(provider.fiveDay(location, units)).thenReturn(listOf(weatherDay))

        val repository = WeatherRepositoryImpl(provider)

        repository.weather()
    }

    @Test(expected = Exception::class)
    fun weather_shouldThrowExceptionWhenFiveDayFail() {
        `when`(provider.fiveDay(location, units)).thenThrow(Exception())

        val repository = WeatherRepositoryImpl(provider)

        repository.weather()
    }
}