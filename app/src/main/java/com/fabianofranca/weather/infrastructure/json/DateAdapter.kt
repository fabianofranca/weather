package com.fabianofranca.weather.infrastructure.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

class DateAdapter {

    @ToJson
    fun toJson(date: Date): Long = date.time / 1000

    @FromJson
    fun fromJson(date: Long): Date = Calendar.getInstance().apply {
        timeInMillis = date * 1000
    }.time
}