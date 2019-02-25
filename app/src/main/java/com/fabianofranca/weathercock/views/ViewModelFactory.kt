package com.fabianofranca.weathercock.views

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.fabianofranca.weathercock.infrastructure.DependencyProvider

class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(viewModelClass: Class<T>): T {

        val viewModel = DependencyProvider.Current.viewModels()[viewModelClass]

        viewModel?.let {
            try {
                return viewModelClass.cast(viewModel)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        } ?: run {
            throw IllegalArgumentException("unknown model class $viewModelClass")
        }
    }
}