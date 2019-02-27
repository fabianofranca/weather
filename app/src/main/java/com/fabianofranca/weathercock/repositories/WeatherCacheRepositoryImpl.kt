package com.fabianofranca.weathercock.repositories

import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Weather
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.providers.room.Database

class WeatherCacheRepositoryImpl(
    private val database: Database = DependencyProvider.Current.database()
): WeatherCacheRepository {

    override fun pull(location: Location): Weather {

        val dao = database.weatherDao()

        return dao.getCurrent(location).apply {
            fiveDays = dao.getFiveDays(location)
        }
    }

    override fun push(location: Location, current: Weather, fiveDays: List<Weather>) {

        database.weatherDao().deleteAll(location)

        val weathers = mutableListOf(current).apply {
            addAll(fiveDays)
        }

        database.weatherDao().insertAll(weathers)
    }
}