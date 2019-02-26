package com.fabianofranca.weathercock.views.weather

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.DrawableRes

class DayViewModel(
    private val index: Int,
    private val selectedIndex: MutableLiveData<Int>,
    val day: String, @DrawableRes val icon: Int,
    val temperature: String,
    val color: LiveData<Int>
) {
    fun select() {
        selectedIndex.value = index
    }
}