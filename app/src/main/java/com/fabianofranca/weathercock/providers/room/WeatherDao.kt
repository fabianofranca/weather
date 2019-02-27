package com.fabianofranca.weathercock.providers.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Weather

@Dao
interface WeatherDao {

    @Insert
    fun insertAll(weathers: List<Weather>)

    @Query("DELETE FROM weather WHERE location = :location")
    fun deleteAll(location: Location)

    @Query("SELECT * FROM weather WHERE location = :location AND type = 'FIVE_DAYS'")
    fun getFiveDays(location: Location): List<Weather>

    @Query("SELECT * FROM weather WHERE location = :location AND type = 'CURRENT' LIMIT 1")
    fun getCurrent(location: Location): Weather
}