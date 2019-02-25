package com.fabianofranca.weathercock.views.weather

import android.arch.lifecycle.LiveData
import android.support.annotation.DrawableRes

data class Day(
    val day: String, @DrawableRes val icon: Int,
    val temperature: String,
    val color: LiveData<Int>
)