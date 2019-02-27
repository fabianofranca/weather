package com.fabianofranca.weathercock.repositories

import com.fabianofranca.weathercock.entities.Profile

interface ProfileRepository {

    fun get(): Profile

    fun update(profile: Profile)
}