package com.fabianofranca.weathercock.views.weather

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.support.v4.content.ContextCompat
import androidx.annotation.DrawableRes
import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.UnitTestMockDependencyProvider
import com.fabianofranca.weathercock.entities.*
import com.fabianofranca.weathercock.extensions.await
import com.fabianofranca.weathercock.extensions.verify
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.repositories.WeatherRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.text.SimpleDateFormat
import java.util.*

@RunWith(RobolectricTestRunner::class)
class WeatherViewModelTest {

    private val application: Application = RuntimeEnvironment.application

    @Mock
    private lateinit var repository: WeatherRepository

    init {
        UnitTestMockDependencyProvider()
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun currentTemperature_shouldWork() {

        val weather = MutableLiveData<Weather>().apply {
            value = Weather(
                Location.SILVERSTONE,
                WeatherType.CURRENT,
                Clear,
                26,
                Calendar.getInstance().time
            )
        }

        `when`(repository.weather()).thenReturn(weather)

        `when`(repository.updated).thenReturn(MutableLiveData<Date>().apply {
            Calendar.getInstance().time
        })

        val viewModel = WeatherViewModel(application, DependencyProvider.Current.bus(), repository)

        viewModel.weatherForecasts.await()

        viewModel.selectedDayIndex.value = 0

        viewModel.temperature.verify {
            assertEquals("26°", it)
        }
    }

    @Test
    fun currentCondition_shouldWork() {
        val weather = MutableLiveData<Weather>().apply {
            value = Weather(
                Location.SILVERSTONE,
                WeatherType.CURRENT,
                Clear,
                26,
                Calendar.getInstance().time
            )
        }

        `when`(repository.weather()).thenReturn(weather)

        `when`(repository.updated).thenReturn(MutableLiveData<Date>().apply {
            Calendar.getInstance().time
        })

        val viewModel = WeatherViewModel(application, DependencyProvider.Current.bus(), repository)

        viewModel.weatherForecasts.await()

        viewModel.selectedDayIndex.value = 0

        viewModel.condition.verify {
            assertEquals("clear", it)
        }
    }

    @Test
    fun currentIconCondition_shouldReturnThunderstorm() {
        iconConditionTest(Thunderstorm, R.drawable.ic_thunerstorm)
    }

    @Test
    fun currentIconCondition_shouldReturnDrizzle() {
        iconConditionTest(Drizzle, R.drawable.ic_drizzle)
    }

    @Test
    fun currentIconCondition_shouldReturnRain() {
        iconConditionTest(Rain, R.drawable.ic_rain)
    }

    @Test
    fun currentIconCondition_shouldReturnSnow() {
        iconConditionTest(Snow, R.drawable.ic_snow)
    }

    @Test
    fun currentIconCondition_shouldReturnAtmosphere() {
        iconConditionTest(Atmosphere, R.drawable.ic_atmosphere)
    }

    @Test
    fun currentIconCondition_shouldReturnClear() {
        iconConditionTest(Clear, R.drawable.ic_clear)
    }

    @Test
    fun currentIconCondition_shouldReturnClouds() {
        iconConditionTest(Clouds, R.drawable.ic_clouds)
    }

    @Test
    fun currentIconCondition_shouldReturnUndefined() {
        iconConditionTest(Undefined, R.drawable.ic_weathercock)
    }

    private fun iconConditionTest(condition: WeatherCondition, @DrawableRes drawable: Int) {
        val weather = MutableLiveData<Weather>().apply {
            value = Weather(Location.SILVERSTONE, WeatherType.CURRENT, condition, 26, Calendar.getInstance().time)
        }

        `when`(repository.weather()).thenReturn(weather)

        `when`(repository.updated).thenReturn(MutableLiveData<Date>().apply {
            Calendar.getInstance().time
        })

        `when`(repository.failure).thenReturn(MutableLiveData<Exception>())


        val viewModel = WeatherViewModel(application, DependencyProvider.Current.bus(), repository)

        viewModel.weatherForecasts.await()

        viewModel.selectedDayIndex.value = 0

        viewModel.iconCondition.verify {
            assertEquals(drawable, it)
        }
    }

    @Test
    fun currentLocation_shouldWork() {
        val location = MutableLiveData<Location>().apply {
            value = Location.SAO_PAULO
        }

        `when`(repository.location).thenReturn(location)

        `when`(repository.updated).thenReturn(MutableLiveData<Date>().apply {
            Calendar.getInstance().time
        })


        val viewModel = WeatherViewModel(application, DependencyProvider.Current.bus(), repository)

        viewModel.location.verify {
            assertEquals("são paulo, br", it)
        }
    }

    @Test
    fun days_shouldWorkToThunderstorm() {
        dayTest(Thunderstorm, R.drawable.ic_thunerstorm_line)
    }

    @Test
    fun days_shouldWorkToDrizzle() {
        dayTest(Drizzle, R.drawable.ic_drizzle_line)
    }

    @Test
    fun days_shouldWorkToRain() {
        dayTest(Rain, R.drawable.ic_rain_line)
    }

    @Test
    fun days_shouldWorkToSnow() {
        dayTest(Snow, R.drawable.ic_snow_line)
    }

    @Test
    fun days_shouldWorkToAtmosphere() {
        dayTest(Atmosphere, R.drawable.ic_atmosphere_line)
    }

    @Test
    fun days_shouldWorkToClear() {
        dayTest(Clear, R.drawable.ic_clear_line)
    }

    @Test
    fun days_shouldWorkToClouds() {
        dayTest(Clouds, R.drawable.ic_clouds_line)
    }

    @Test
    fun days_shouldWorkToUndefined() {
        dayTest(Undefined, R.drawable.ic_weathercock_line)
    }

    private fun dayTest(condition: WeatherCondition, @DrawableRes drawable: Int) {
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DATE, 1)
        }.time

        val weather = MutableLiveData<Weather>().apply {

            val day = Weather(Location.SILVERSTONE, WeatherType.FIVE_DAYS, condition, 25, tomorrow)

            value = Weather(Location.SILVERSTONE, WeatherType.CURRENT, condition, 26, Calendar.getInstance().time, 0F, 0).apply {
                fiveDays = listOf(day)
            }
        }

        `when`(repository.weather()).thenReturn(weather)

        `when`(repository.updated).thenReturn(MutableLiveData<Date>().apply {
            Calendar.getInstance().time
        })

        val viewModel = WeatherViewModel(application, DependencyProvider.Current.bus(), repository)

        viewModel.selectedDayIndex.value = 1

        viewModel.selectedDayIndex.await()

        viewModel.days.verify {
            val expectedDay = SimpleDateFormat("dd/MM", Locale.US).format(tomorrow)

            assertEquals(drawable, it?.get(0)?.icon)
            assertEquals("today", it?.get(0)?.day)
            assertEquals("26°", it?.get(0)?.temperature)

            it?.get(0)?.color?.verify { color ->
                assertEquals(
                    ContextCompat.getColor(application, R.color.colorSecondary),
                    color
                )
            }

            assertEquals(drawable, it?.get(1)?.icon)
            assertEquals(expectedDay, it?.get(1)?.day)
            assertEquals("25°", it?.get(1)?.temperature)

            it?.get(1)?.color?.verify { color ->
                assertEquals(
                    ContextCompat.getColor(application, R.color.colorPrimary),
                    color
                )
            }
        }
    }
}