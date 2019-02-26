package com.fabianofranca.weathercock.views.locations


import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.databinding.FragmentLocationsBinding
import com.fabianofranca.weathercock.infrastructure.DependencyProvider

class LocationsFragment : Fragment() {

    private lateinit var binding: FragmentLocationsBinding

    private val factory = DependencyProvider.Current.viewModelFactory()

    private lateinit var viewModel: LocationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_locations, container, false)

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory)[LocationsViewModel::class.java]

        binding.viewModel = viewModel
    }
}
