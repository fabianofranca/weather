package com.fabianofranca.weathercock.view.weather


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.view.home.ChangePageEvent
import com.fabianofranca.weathercock.view.home.Page
import kotlinx.android.synthetic.main.fragment_weather.view.*

class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        ViewCompat.setTransitionName(view.logo, "logo")

        view.arrowDown.setOnClickListener {
            DependencyProvider.Current.bus().post(ChangePageEvent(Page.LOCATION))
        }

        return view
    }
}
