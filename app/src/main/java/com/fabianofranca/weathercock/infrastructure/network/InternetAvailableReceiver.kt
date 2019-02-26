package com.fabianofranca.weathercock.infrastructure.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fabianofranca.weathercock.infrastructure.DependencyProvider


class InternetAvailableReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        DependencyProvider.Current.bus().post(InternetAvailableEvent(DependencyProvider.Current.connected()))
    }

}