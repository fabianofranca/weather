package com.fabianofranca.weathercock.views.locations

import android.arch.lifecycle.LiveData
import com.fabianofranca.weathercock.entities.Location

data class LocationViewModel(val location: Location, val color: LiveData<Int>)