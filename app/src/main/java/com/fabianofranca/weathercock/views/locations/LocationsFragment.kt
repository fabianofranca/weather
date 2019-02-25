package com.fabianofranca.weathercock.views.locations


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.entities.Location
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.views.weather.ReloadWeatherForecastsEvent
import kotlinx.android.synthetic.main.fragment_locations.view.*

class LocationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_locations, container, false)

        setupMenu(view)

        return view
    }

    fun setupMenu(view: View) {
        view.silverstoneText.setOnClickListener {
            DependencyProvider.Current.injectLocation(Location.SILVERSTONE)
            DependencyProvider.Current.bus().post(ReloadWeatherForecastsEvent())
        }

        view.saoPauloText.setOnClickListener {
            DependencyProvider.Current.injectLocation(Location.SAO_PAULO)
            DependencyProvider.Current.bus().post(ReloadWeatherForecastsEvent())
        }

        view.melbourneText.setOnClickListener {
            DependencyProvider.Current.injectLocation(Location.MELBOURNE)
            DependencyProvider.Current.bus().post(ReloadWeatherForecastsEvent())
        }

        view.monacoText.setOnClickListener {
            DependencyProvider.Current.injectLocation(Location.MONACO)
            DependencyProvider.Current.bus().post(ReloadWeatherForecastsEvent())
        }
    }
}
