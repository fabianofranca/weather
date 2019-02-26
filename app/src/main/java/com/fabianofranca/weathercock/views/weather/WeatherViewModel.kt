package com.fabianofranca.weathercock.views.weather

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.v4.content.ContextCompat
import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.entities.*
import com.fabianofranca.weathercock.infrastructure.network.InternetAvailableEvent
import com.fabianofranca.weathercock.repositories.WeatherRepository
import com.fabianofranca.weathercock.repositories.WeatherRepositoryImpl
import com.fabianofranca.weathercock.views.home.ChangePageEvent
import com.fabianofranca.weathercock.views.home.Page
import com.fabianofranca.weathercock.views.locations.ChangeLocationEvent
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(
    application: Application,
    private val bus: Bus,
    private val repository: WeatherRepository = WeatherRepositoryImpl()
) : AndroidViewModel(application) {

    val selectedDayIndex = MutableLiveData<Int>()

    private val degree = application.getString(R.string.degree)

    private val selectedColor = ContextCompat.getColor(application, R.color.colorPrimary)

    private val unselectedColor = ContextCompat.getColor(application, R.color.colorSecondary)

    val weatherForecasts: LiveData<List<Weather>> = Transformations.map(repository.weather()) {

        it?.let { w ->
            _syncStatus.value = SyncStatus.ONLINE

            val days = mutableListOf(w)

            w.fiveDays?.let { d ->
                days.addAll(d)
            }

            days
        }
    }

    private val weatherForecast = Transformations.switchMap(selectedDayIndex) { index ->
        MutableLiveData<Weather>().apply {
            value = weatherForecasts.value?.get(index)
        }
    }

    val temperature: LiveData<String> = Transformations.map(weatherForecast) {
        "${it.temperature}$degree"
    }

    val condition: LiveData<String> = Transformations.map(weatherForecast) {
        it.condition.description.toLowerCase()
    }

    val iconCondition: LiveData<Int> = Transformations.map(weatherForecast) {
        conditionIcon(it.condition)
    }

    val location: LiveData<String> = Transformations.map(repository.location()) {
        it?.let { location ->
            bus.post(ChangeLocationEvent(location))
        }

        it?.value?.toLowerCase()
    }

    val days: LiveData<List<DayViewModel>> = Transformations.map(weatherForecasts) {
        val formatter = SimpleDateFormat("dd/MM", Locale.US)

        var hasToday = false

        it.mapIndexed { index, w ->
            val today = formatter.format(Calendar.getInstance().time)
            val item = formatter.format(w.date)

            val day = if (today == item && !hasToday) {
                hasToday = true
                application.getString(R.string.today)
            } else
                item
            val degree = application.getString(R.string.degree)

            val color: LiveData<Int> = Transformations.map(this.selectedDayIndex) { dayIndex ->
                if (dayIndex == index) selectedColor else unselectedColor
            }

            DayViewModel(
                index,
                selectedDayIndex,
                day,
                dayIcon(w.condition),
                "${w.temperature}$degree",
                color
            )
        }
    }

    private val _syncStatus = MutableLiveData<SyncStatus>().apply { value = SyncStatus.LOADING }

    val syncStatus: LiveData<SyncStatus>
        get() {
            return _syncStatus
        }

    val syncIcon: LiveData<Int> = Transformations.map(_syncStatus) {
        it?.let { status ->
            when (status) {
                SyncStatus.LOADING, SyncStatus.ONLINE -> R.drawable.ic_sync
                SyncStatus.OFFLINE -> R.drawable.ic_offline
            }
        }
    }

    val failure: LiveData<String> = Transformations.map(repository.failure) {
        it?.let { e ->
            _syncStatus.value = SyncStatus.ONLINE
            application.getString(R.string.failure)
        }
    }

    init {
        bus.register(this)
    }

    private fun conditionIcon(condition: WeatherCondition) = when (condition) {
        Thunderstorm -> R.drawable.ic_thunerstorm
        Drizzle -> R.drawable.ic_drizzle
        Rain -> R.drawable.ic_rain
        Snow -> R.drawable.ic_snow
        Atmosphere -> R.drawable.ic_atmosphere
        Clear -> R.drawable.ic_clear
        Clouds -> R.drawable.ic_clouds
        Undefined -> R.drawable.ic_weathercock
    }

    private fun dayIcon(condition: WeatherCondition) = when (condition) {
        Thunderstorm -> R.drawable.ic_thunerstorm_line
        Drizzle -> R.drawable.ic_drizzle_line
        Rain -> R.drawable.ic_rain_line
        Snow -> R.drawable.ic_snow_line
        Atmosphere -> R.drawable.ic_atmosphere_line
        Clear -> R.drawable.ic_clear_line
        Clouds -> R.drawable.ic_clouds_line
        Undefined -> R.drawable.ic_weathercock_line
    }

    fun sync() {
        sync(null)
    }

    private fun sync(location: Location?): Boolean {
        _syncStatus.value?.let {
            if (it != SyncStatus.ONLINE) return@sync false
        }

        _syncStatus.value = SyncStatus.LOADING

        location?.let {
            repository.weather(it)
        } ?: run {
            repository.weather()
        }

        return true
    }

    @Subscribe
    fun syncSubscribe(event: SyncEvent) {
        if (sync(event.location)) bus.post(ChangePageEvent(Page.WEATHER))
    }

    @Subscribe
    fun internetAvaiableSubscribe(event: InternetAvailableEvent) {
        _syncStatus.value = if (event.connected) _syncStatus.value else SyncStatus.OFFLINE
    }
}