package com.fabianofranca.weathercock.providers.room

import android.arch.persistence.room.TypeConverter
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.UviType
import com.fabianofranca.weathercock.entities.WeatherCondition
import com.fabianofranca.weathercock.entities.WeatherType
import java.util.*

class RoomConverters {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromLocation(location: Location) = location.name

        @TypeConverter
        @JvmStatic
        fun toLocation(value: String) = Location.valueOf(value)

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date?) = date?.time

        @TypeConverter
        @JvmStatic
        fun toDate(value: Long?) : Date? {
            value?.let {

                val calendar = Calendar.getInstance()

                calendar.timeInMillis = it

                return calendar.time
            }

            return null
        }

        @TypeConverter
        @JvmStatic
        fun fromWeatherCondition(condition: WeatherCondition) = condition.ids.first

        @TypeConverter
        @JvmStatic
        fun toWeatherCondition(value: Int) = WeatherCondition.fromId(value)

        @TypeConverter
        @JvmStatic
        fun fromWeatherType(type: WeatherType) = type.name

        @TypeConverter
        @JvmStatic
        fun toWeatherType(value: String) = WeatherType.valueOf(value)

        @TypeConverter
        @JvmStatic
        fun fromUvi(uvi: UviType) = uvi.start

        @TypeConverter
        @JvmStatic
        fun toUvi(value: Float) = UviType.fromValue(value)

    }
}