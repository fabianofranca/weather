package com.fabianofranca.weathercock.views.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.views.locations.LocationsFragment
import com.fabianofranca.weathercock.views.weather.WeatherFragment
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DependencyProvider.Current.bus().register(this)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.viewPager.scrollBarFadeDuration = 1000

        fragmentManager?.let {
            view.viewPager.adapter = HomePageAdapter(it)
            view.viewPager.currentItem = 1
        }

        return view
    }

    private class HomePageAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

        override fun getItem(position: Int) = when (position) {
            0 -> LocationsFragment()
            1 -> WeatherFragment()
            else -> throw Exception("Not found fragment to position $position")
        }

        override fun getCount() = 2
    }

    @Subscribe
    fun changePage(event: ChangePageEvent) {
        viewPager.setCurrentItem(event.page.position, true)
    }
}
