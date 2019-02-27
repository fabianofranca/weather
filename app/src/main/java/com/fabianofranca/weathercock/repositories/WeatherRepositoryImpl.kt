package com.fabianofranca.weathercock.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Weather
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.providers.WeatherProvider
import com.fabianofranca.weathercock.providers.WeatherProviderImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class WeatherRepositoryImpl(
    private val weatherApiProvider: WeatherProvider = WeatherProviderImpl(),
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl(),
    private val locationsUpdateRepository: LocationsUpdateRepository = LocationsUpdateRepositoryImpl(),
    private val weatherCacheRepository: WeatherCacheRepository = WeatherCacheRepositoryImpl()
) :
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

            val profile = withContext(DependencyProvider.Current.ioDispatcher()) {
                profileRepository.get()
            }

            val newLocation = location ?: profile.location

            try {

                if (DependencyProvider.Current.connected()) {

                    val current = withContext(DependencyProvider.Current.ioDispatcher()) {
                        weatherApiProvider.current(newLocation, units)
                    }

                    val fiveDays = withContext(DependencyProvider.Current.ioDispatcher()) {
                        weatherApiProvider.fiveDay(newLocation, units)
                    }

                    weather.value = current.apply { this.fiveDays = fiveDays }

                    withContext(DependencyProvider.Current.ioDispatcher()) {
                        weatherCacheRepository.push(newLocation, current, fiveDays)
                    }

                    val date = withContext(DependencyProvider.Current.ioDispatcher()) {
                        locationsUpdateRepository.update(newLocation)
                    }

                    _updated.value = date

                    if (profile.location != newLocation) {
                        profile.location = newLocation
                    }
                } else {
                    loadCache(newLocation)
                }


            } catch (e: Exception) {

                loadCache(newLocation)

                if (DependencyProvider.Current.connected()) {
                    _failure.value = e
                }
            }

            profile.location = newLocation

            withContext(DependencyProvider.Current.ioDispatcher()) {
                profileRepository.update(profile)
            }

            _location.value = newLocation
        }

        return weather
    }

    private suspend fun loadCache(location: Location) =
        withContext(DependencyProvider.Current.uiDispatcher()) {

            val date = withContext(DependencyProvider.Current.ioDispatcher()) {
                locationsUpdateRepository.date(location)
            }

            date?.let {
                val cache = async(DependencyProvider.Current.ioDispatcher()) {
                    weatherCacheRepository.pull(location)
                }

                weather.value = cache.await()

                _updated.value = it

                _location.value = location

            } ?: run {
                weather.value = null
                _updated.value = null
            }
        }
}