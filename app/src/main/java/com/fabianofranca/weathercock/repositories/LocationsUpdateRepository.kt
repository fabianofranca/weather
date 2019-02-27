package com.fabianofranca.weathercock.repositories

import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.LocationsUpdate
import java.util.*

interface LocationsUpdateRepository {

    fun date(location: Location): Date?

    fun update(location: Location) : Date
}