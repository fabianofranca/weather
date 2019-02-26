package com.fabianofranca.weathercock.views.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe

class HomeViewModel(bus: Bus) : ViewModel() {

    init {
        bus.register(this)
    }

    private val _page = MutableLiveData<Page>()

    val page: LiveData<Page>
        get() {
            return _page
        }

    @Subscribe
    fun changePageSubscribe(event: ChangePageEvent) {
        _page.value = event.page
    }
}