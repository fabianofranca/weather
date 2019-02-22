package com.fabianofranca.weather.repositories

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.fabianofranca.weather.entities.Clear
import com.fabianofranca.weather.entities.Locale
import com.fabianofranca.weather.entities.Weather
import com.fabianofranca.weather.extensions.verify
import com.fabianofranca.weather.providers.WeatherProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryImplTest {

    @Rule
    @JvmField
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    lateinit var provider: WeatherProvider

    private val weather = Weather(Clear, 0F, Calendar.getInstance().time)

    private val locale = Locale.SILVERSTONE

    @Test
    fun current_shouldWork() {

        `when`(provider.current(locale)).thenReturn(weather)

        val repository = WeatherRepositoryImpl(provider)

        repository.current(Locale.SILVERSTONE).verify {
            assertEquals(weather, it)
        }
    }

    @Test(expected = Exception::class)
    fun current_shouldThrowException() {
        `when`(provider.current(locale)).thenThrow(Exception())

        val repository = WeatherRepositoryImpl(provider)

        repository.current(locale)
    }

    @Test
    fun fiveDay_shouldWork() {

        `when`(provider.fiveDay(locale)).thenReturn(listOf(weather))

        val repository = WeatherRepositoryImpl(provider)

        repository.fiveDay(Locale.SILVERSTONE).verify {
            assertEquals(weather, it!!.first())
        }
    }

    @Test(expected = Exception::class)
    fun fiveDay_shouldThrowException() {
        `when`(provider.fiveDay(locale)).thenThrow(Exception())

        val repository = WeatherRepositoryImpl(provider)

        repository.fiveDay(locale)
    }
}