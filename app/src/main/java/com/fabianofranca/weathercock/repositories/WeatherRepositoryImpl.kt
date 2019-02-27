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
import java.util.*

class WeatherRepositoryImpl(private val provider: WeatherProvider = WeatherApiProvider()) :
    WeatherRepository {

    private val weather = MutableLiveData<Weather>()

    private val _location = MutableLiveData<Location>()

    private val _failure = MutableLiveData<Exception>()

    private val _updated = MutableLiveData<Date>()

    override val location: LiveData<Location>
        get() {
            return _location
        }

    override val failure: LiveData<Exception>
        get() {
            return _failure
        }

    override val updated: LiveData<Date>
        get() {
            return _updated
        }

    override fun weather(location: Location?): LiveData<Weather> {
        val units = DependencyProvider.Current.units()

        GlobalScope.launch(DependencyProvider.Current.uiDispatcher()) {

            try {
                var newLocation = Location.SILVERSTONE

                location?.let {
                    newLocation = it
                } ?: run {
                    _location.value?.let {
                        newLocation = it
                    }
                }

                val current = currentAsync(newLocation, units)
                val fiveDays = fiveDaysAsync(newLocation, units)

                weather.value = current.await().apply { this.fiveDays = fiveDays.await() }

                _updated.value = Calendar.getInstance().time

                if (_location.value != newLocation) {
                    _location.value = newLocation
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
}