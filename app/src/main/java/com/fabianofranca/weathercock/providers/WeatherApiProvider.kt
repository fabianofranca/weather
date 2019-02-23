package com.fabianofranca.weathercock.providers

import com.fabianofranca.weathercock.entities.Locale
import com.fabianofranca.weathercock.entities.Units
import com.fabianofranca.weathercock.entities.Weather
import com.fabianofranca.weathercock.entities.WeatherCondition
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.infrastructure.api.WeatherApi
import java.util.*

class WeatherApiProvider(
    private val api: WeatherApi = DependencyProvider.Current.api(),
    private val apiKey: String = DependencyProvider.Current.apiKey(),
    private val units: Units = DependencyProvider.Current.units()
) : WeatherProvider {

    override fun current(locale: Locale): Weather {
        val raw = api.weather(locale.value, apiKey, units.value).execute()

        raw.body()?.let { r ->
            return Weather(WeatherCondition.fromId(r.weather.first().id), r.main.temp, r.dt)
        }

        throw Exception()
    }

    override fun fiveDay(locale: Locale): List<Weather> {

        val raw = api.forecast(locale.value, apiKey, units.value).execute()

        raw.body()?.let { r ->
            return r.list.map {
                Weather(WeatherCondition.fromId(it.weather.first().id), it.main.temp, it.dt)
            }.filter {
                val calendar = Calendar.getInstance().apply { time = it.date }

                calendar.get(Calendar.HOUR_OF_DAY) == 12 &&
                        calendar.get(Calendar.MINUTE) == 0 &&
                        calendar.get(Calendar.SECOND) == 0
            }
        }

        throw Exception()
    }
}