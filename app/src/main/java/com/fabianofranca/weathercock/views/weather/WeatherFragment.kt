package com.fabianofranca.weathercock.views.weather


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.databinding.FragmentWeatherBinding
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.views.home.ChangePageEvent
import com.fabianofranca.weathercock.views.home.Page

class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentWeatherBinding

    private val factory = DependencyProvider.Current.viewModelFactory()

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)

        binding.lifecycleOwner = this

        ViewCompat.setTransitionName(binding.logo, "logo")

        binding.arrowDown.setOnClickListener {
            DependencyProvider.Current.bus().post(ChangePageEvent(Page.LOCATION))
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory)[WeatherViewModel::class.java]

        binding.viewModel = viewModel

        setupObservers()
    }

    private fun setupObservers() {

        viewModel.weatherForecasts.observe(this, Observer {
            viewModel.selectedDayIndex.value = 0
        })

        viewModel.sync.observe(this, Observer {
            it?.let { sync ->

                if (sync) {
                    binding.sync.startAnimation(
                        AnimationUtils.loadAnimation(
                            activity,
                            R.anim.rotate
                        )
                    )
                } else {
                    binding.sync.animation?.cancel()
                }
            }
        })
    }
}
