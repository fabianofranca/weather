package com.fabianofranca.weathercock.views.weather

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.v4.content.ContextCompat
import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.entities.*
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.repositories.WeatherRepository
import com.fabianofranca.weathercock.repositories.WeatherRepositoryImpl
import com.fabianofranca.weathercock.views.home.ChangePageEvent
import com.fabianofranca.weathercock.views.home.Page
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

        _sync.value = false

        it?.let { w ->
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

    private val _location = MutableLiveData<String>().apply {
        value = DependencyProvider.Current.location().value.toLowerCase()
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

    val location: LiveData<String>
        get() {
            return _location
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

    private val _sync = MutableLiveData<Boolean>().apply { value = true }

    val sync: LiveData<Boolean>
        get() {
            return _sync
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

    fun sync() : Boolean {
        _sync.value?.let {
            if (it) return@sync false
        }

        _sync.value = true
        repository.weather()
        return true
    }

    @Subscribe
    fun sync(event: SyncEvent) {
        if (sync()) {
            _location.value = DependencyProvider.Current.location().value
            bus.post(ChangePageEvent(Page.WEATHER))
        }
    }
}