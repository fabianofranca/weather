package com.fabianofranca.weathercock.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class Weather(
    var location: Location,
    var type: WeatherType,
    var condition: WeatherCondition,
    var temperature: Int,
    var date: Date,
    var uvi: UviType = Indeterminate,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
    @Ignore
    var fiveDays: List<Weather>? = null

    @Ignore
    var latitude: Double = 0.0

    @Ignore
    var longitude: Double = 0.0
}