package com.fabianofranca.weathercock.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
data class LocationsUpdate(
    @PrimaryKey @ColumnInfo(name = "location") var location: Location,
    @ColumnInfo(name = "date") var date: Date
)