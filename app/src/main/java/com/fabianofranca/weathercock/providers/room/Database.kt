package com.fabianofranca.weathercock.providers.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.fabianofranca.weathercock.entities.LocationsUpdate
import com.fabianofranca.weathercock.entities.Profile
import com.fabianofranca.weathercock.entities.Weather

@Database(entities = [Profile::class, LocationsUpdate::class, Weather::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class Database : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun locationsUpdateDao(): LocationsUpdateDao
    abstract fun weatherDao(): WeatherDao
}