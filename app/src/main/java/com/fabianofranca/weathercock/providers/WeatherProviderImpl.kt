package com.fabianofranca.weathercock.providers

import com.fabianofranca.weathercock.entities.*
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.infrastructure.api.UviRaw
import com.fabianofranca.weathercock.infrastructure.api.WeatherApi
import java.util.*
import kotlin.math.roundToInt

class WeatherProviderImpl(
    private val api: WeatherApi = DependencyProvider.Current.api(),
    private val apiKey: String = DependencyProvider.Current.apiKey()
) : WeatherProvider {

    override fun current(location: Location, units: Units): Weather {

        val raw = api.weather(location.value, apiKey, units.value).execute()

        raw.body()?.let { r ->
            return Weather(
                location,
                WeatherType.CURRENT,
                WeatherCondition.fromId(r.weather.first().id),
                r.main.temp.roundToInt(),
                r.dt
            ).apply {
                r.coord?.let {
                    latitude = it.lat
                    longitude = it.lon
                }
            }
        }

        throw Exception()
    }

    override fun fiveDay(location: Location, units: Units): List<Weather> {

        val raw = api.forecast(location.value, apiKey, units.value).execute()

        raw.body()?.let { r ->
            return r.list.map {
                Weather(
                    location,
                    WeatherType.FIVE_DAYS,
                    WeatherCondition.fromId(it.weather.first().id),
                    it.main.temp.roundToInt(),
                    it.dt
                )
            }.filter {
                val calendar = Calendar.getInstance().apply { time = it.date }

                calendar.get(Calendar.HOUR_OF_DAY) == 12 &&
                        calendar.get(Calendar.MINUTE) == 0 &&
                        calendar.get(Calendar.SECOND) == 0
            }
        }

        throw Exception()
    }

    override fun uvi(latitude: Double, longitude: Double): UviRaw {
        val raw  = api.uvi(apiKey, latitude, longitude).execute()

        raw.body()?.let {
           return it
        }

        throw Exception()
    }
}