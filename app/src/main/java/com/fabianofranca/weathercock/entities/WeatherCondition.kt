package com.fabianofranca.weathercock.entities

sealed class WeatherCondition(val ids: IntRange, val description: String) {

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

object Thunderstorm : WeatherCondition(200..232, "Thunderstorm")

object Drizzle : WeatherCondition(300..321, "Drizzle")

object Rain : WeatherCondition(500..531, "Rain")

object Snow : WeatherCondition(600..622, "Snow")

object Atmosphere : WeatherCondition(701..781, "Mist")

object Clear : WeatherCondition(800..800, "Clear")

object Clouds : WeatherCondition(801..804, "Clouds")

object Undefined : WeatherCondition(0..0, "Undefined")