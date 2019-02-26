package com.fabianofranca.weathercock.views.locations

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.v4.content.ContextCompat
import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.views.weather.SyncEvent
import com.squareup.otto.Bus

class LocationsViewModel(application: Application, private val bus: Bus) :
    AndroidViewModel(application) {

    private val location =
        MutableLiveData<Location>().apply { value = DependencyProvider.Current.location() }

    private val selectedColor = ContextCompat.getColor(application, R.color.colorPrimary)

    private val unselectedColor = ContextCompat.getColor(application, R.color.colorSecondary)


    val locations = listOf(
        LocationViewModel(Location.SILVERSTONE, Transformations.map(location) {
            if (it == Location.SILVERSTONE) selectedColor else unselectedColor
        }),
        LocationViewModel(Location.SAO_PAULO, Transformations.map(location) {
            if (it == Location.SAO_PAULO) selectedColor else unselectedColor
        }),
        LocationViewModel(Location.MELBOURNE, Transformations.map(location) {
            if (it == Location.MELBOURNE) selectedColor else unselectedColor
        }),
        LocationViewModel(Location.MONACO, Transformations.map(location) {
            if (it == Location.MONACO) selectedColor else unselectedColor
        })
    )

    fun updateCurrentLocation(location: Location) {
        DependencyProvider.Current.injectLocation(location)
        this.location.value = location
        bus.post(SyncEvent())
    }
}