package com.fabianofranca.weathercock.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.entities.Weather
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.providers.WeatherApiProvider
import com.fabianofranca.weathercock.providers.WeatherProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class WeatherRepositoryImpl(private val provider: WeatherProvider = WeatherApiProvider()) :
    WeatherRepository {

    private val weather = MutableLiveData<Weather>()

    override fun weather(): LiveData<Weather> {
        val location = DependencyProvider.Current.location()
        val units = DependencyProvider.Current.units()

        GlobalScope.launch(DependencyProvider.Current.uiDispatcher()) {
            val current = currentAsync(location, units)
            val fiveDays = fiveDaysAsync(location, units)

            weather.value = current.await().apply { this.fiveDays = fiveDays.await() }
        }

        return weather
    }

    private fun currentAsync(location: Location, units: Units) =
        GlobalScope.async(DependencyProvider.Current.ioDispatcher()) {
            provider.current(
                location,
                units
            )
        }

    private fun fiveDaysAsync(location: Location, units: Units) =
        GlobalScope.async(DependencyProvider.Current.ioDispatcher()) {
            provider.fiveDay(
                location,
                units
            )
        }
}