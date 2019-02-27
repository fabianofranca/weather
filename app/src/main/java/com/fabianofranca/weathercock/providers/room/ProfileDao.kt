package com.fabianofranca.weathercock.providers.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.fabianofranca.weathercock.entities.Profile

@Dao
interface ProfileDao {

    @Insert
    fun insert(profile: Profile)

    @Update
    fun update(profile: Profile)

    @Query("SELECT * FROM profile LIMIT 1")
    fun get(): Profile?
}