package com.fabianofranca.weathercock.providers.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.LocationsUpdate

@Dao
interface LocationsUpdateDao {

    @Insert
    fun insert(locationsUpdate: LocationsUpdate)

    @Update
    fun update(locationsUpdate: LocationsUpdate)

    @Query("SELECT * FROM locationsUpdate WHERE location = :location LIMIT 1")
    fun get(location: Location): LocationsUpdate?
}