package com.fabianofranca.weathercock.repositories

import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.entities.Profile
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.providers.room.Database

class ProfileRepositoryImpl(
    private val database: Database = DependencyProvider.Current.database()
) : ProfileRepository {

    override fun get(): Profile {
        val dao = database.profileDao()

        dao.get()?.let {
            return it
        } ?: run {
            dao.insert(Profile(Location.SILVERSTONE))
            return dao.get()!!
        }
    }

    override fun update(profile: Profile) {
        database.profileDao().update(profile)
    }
}