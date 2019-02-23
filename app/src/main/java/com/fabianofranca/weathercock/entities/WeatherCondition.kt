package com.fabianofranca.weathercock.entities

sealed class WeatherCondition(val ids: IntRange, val description: String = this::class.simpleName!!) {

    companion object {
        fun fromId(id: Int) = when {
            id in Thunderstorm.ids -> Thunderstorm
            id in Drizzle.ids -> Drizzle
            id in Rain.ids -> Rain
            id in Snow.ids -> Snow
            id in Atmosphere.ids -> Atmosphere
            id in Clear.ids -> Clear
            id in Clouds.ids -> Clouds
            else -> Undefined
        }
    }
}

object Thunderstorm: WeatherCondition(200..232)

object Drizzle: WeatherCondition(300..321)

object Rain: WeatherCondition(500..531)

object Snow: WeatherCondition(600..622)

object Atmosphere: WeatherCondition(701..781)

object Clear: WeatherCondition(800..800)

object Clouds: WeatherCondition(801..804)

object Undefined: WeatherCondition(0..0)