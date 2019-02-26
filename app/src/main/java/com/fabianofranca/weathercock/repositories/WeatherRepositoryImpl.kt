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

    private val location = MutableLiveData<Location>()

    private val _failure = MutableLiveData<Exception>()

    override val failure: LiveData<Exception>
        get() {
            return _failure
        }

    override fun weather(location: Location?): LiveData<Weather> {
        val units = DependencyProvider.Current.units()

        GlobalScope.launch(DependencyProvider.Current.uiDispatcher()) {

            try {
                var newLocation = Location.SILVERSTONE

                location?.let {
                    newLocation = it
                } ?: run {
                    this@WeatherRepositoryImpl.location.value?.let {
                        newLocation = it
                    }
                }

                val current = currentAsync(newLocation, units)
                val fiveDays = fiveDaysAsync(newLocation, units)

                weather.value = current.await().apply { this.fiveDays = fiveDays.await() }

                if (this@WeatherRepositoryImpl.location.value != newLocation) {
                    this@WeatherRepositoryImpl.location.value = newLocation
                }
            } catch (e: Exception) {
                _failure.value = e
            }
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

    override fun location() = location
}