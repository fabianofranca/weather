package com.fabianofranca.weathercock.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Profile(
    @ColumnInfo(name = "location") var location: Location,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)