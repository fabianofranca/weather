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

    @Mock
    lateinit var profileRepository: ProfileRepository

    private val weatherCurrent =
        Weather(Location.SILVERSTONE, WeatherType.CURRENT, Clear, 0, Calendar.getInstance().time)

    private val weatherDay =
        Weather(Location.SILVERSTONE, WeatherType.FIVE_DAYS, Rain, 0, Calendar.getInstance().time)

    private val defaultLocation = Location.SILVERSTONE
    private val units = Units.METRIC

    init {
        UnitTestMockDependencyProvider()

        DependencyProvider.Current.injectUnits(units)
    }

    @Test
    fun weather_shouldWork() {

        `when`(provider.current(defaultLocation, units)).thenReturn(weatherCurrent)
        `when`(provider.fiveDay(defaultLocation, units)).thenReturn(listOf(weatherDay))

        `when`(profileRepository.get()).thenReturn(Profile(Location.SILVERSTONE))

        val repository = WeatherRepositoryImpl(provider, profileRepository)

        repository.weather().verify {
            assertEquals(weatherCurrent, it)
            assertEquals(weatherDay, it?.fiveDays?.first())
        }

        verify(provider).current(defaultLocation, units)
        verify(provider).fiveDay(defaultLocation, units)
    }

    @Test(expected = Exception::class)
    fun weather_shouldThrowExceptionWhenCurrentFail() {
        `when`(provider.current(defaultLocation, units)).thenThrow(Exception())
        `when`(provider.fiveDay(defaultLocation, units)).thenReturn(listOf(weatherDay))

        val repository = WeatherRepositoryImpl(provider)

        repository.weather()
    }

    @Test(expected = Exception::class)
    fun weather_shouldThrowExceptionWhenFiveDayFail() {
        `when`(provider.fiveDay(defaultLocation, units)).thenThrow(Exception())

        val repository = WeatherRepositoryImpl(provider)

        repository.weather()
    }
}