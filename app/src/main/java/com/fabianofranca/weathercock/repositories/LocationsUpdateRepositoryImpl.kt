package com.fabianofranca.weathercock.repositories

import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.LocationsUpdate
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.providers.room.Database
import java.util.*

class LocationsUpdateRepositoryImpl(
    private val database: Database = DependencyProvider.Current.database()
) : LocationsUpdateRepository {

    override fun date(location: Location): Date? {

        database.locationsUpdateDao().get(location)?.let {
            return it.date
        }

        return null
    }

    override fun update(location: Location) : Date {
        val dao = database.locationsUpdateDao()

        val date = Calendar.getInstance().time

        dao.get(location)?.let {
            it.date = date
            dao.update(it)
        } ?: run {
            dao.insert(LocationsUpdate(location, date))
        }

        return date
    }
}