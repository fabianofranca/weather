package com.fabianofranca.weathercock.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.fabianofranca.weathercock.entities.Locale
import com.fabianofranca.weathercock.entities.Weather
import com.fabianofranca.weathercock.providers.WeatherApiProvider
import com.fabianofranca.weathercock.providers.WeatherProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherRepositoryImpl(private val provider: WeatherProvider = WeatherApiProvider()) :
    WeatherRepository {

    private val current = MutableLiveData<Weather>()

    private val fiveDay = MutableLiveData<List<Weather>>()

    override fun current(locale: Locale): LiveData<Weather> {

        GlobalScope.launch {
            current.value = provider.current(locale)
        }

        return current
    }

    override fun fiveDay(locale: Locale): LiveData<List<Weather>> {

        GlobalScope.launch {
            fiveDay.value = provider.fiveDay(locale)
        }

        return fiveDay
    }
}