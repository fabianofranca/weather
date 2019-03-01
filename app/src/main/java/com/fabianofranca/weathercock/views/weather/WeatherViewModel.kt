package com.fabianofranca.weathercock.views.weather

import android.app.Application
import android.arch.lifecycle.*
import android.support.v4.content.ContextCompat
import com.fabianofranca.weathercock.R
import com.fabianofranca.weathercock.entities.*
import com.fabianofranca.weathercock.infrastructure.DependencyProvider
import com.fabianofranca.weathercock.infrastructure.network.InternetAvailableEvent
import com.fabianofranca.weathercock.repositories.WeatherRepository
import com.fabianofranca.weathercock.repositories.WeatherRepositoryImpl
import com.fabianofranca.weathercock.views.home.ChangePageEvent
import com.fabianofranca.weathercock.views.home.Page
import com.fabianofranca.weathercock.views.locations.ChangeLocationEvent
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import java.text.SimpleDateFormat
import java.util.*


class WeatherViewModel(
    application: Application,
    private val bus: Bus,
    private val repository: WeatherRepository = WeatherRepositoryImpl()
) : AndroidViewModel(application) {

    val selectedDayIndex = MutableLiveData<Int>()

    private val _application = getApplication<Application>()

    private val degree = application.getString(R.string.degree)

    private val selectedColor = ContextCompat.getColor(application, R.color.colorPrimary)

    private val unselectedColor = ContextCompat.getColor(application, R.color.colorSecondary)

    val weatherForecasts: LiveData<List<Weather>> = Transformations.map(repository.weather()) {

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

    val temperature: LiveData<String> = Transformations.map(weatherForecast) {
        it?.let { weather ->
            "${weather.temperature}$degree"
        }
    }

    val condition: LiveData<String> = Transformations.map(weatherForecast) {
        it?.condition?.description?.toLowerCase()
    }

    private val _iconCondition = MediatorLiveData<Int>()

    val iconCondition: LiveData<Int>
        get() {
            return _iconCondition
        }

    val location: LiveData<String> = Transformations.map(repository.location) {
        it?.let { location ->
            bus.post(ChangeLocationEvent(location))
            location.value.toLowerCase()
        }
    }

    val days: LiveData<List<DayViewModel>> = Transformations.map(weatherForecasts) { weathers ->
        weathers?.let {
            val formatter = SimpleDateFormat(DAY_FORMAT, Locale.US)

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
    }

    private val _syncStatus = MediatorLiveData<SyncStatus>().apply {
        value = SyncStatus.LOADING
    }

    val syncStatus: LiveData<SyncStatus>
        get() {
            return _syncStatus
        }

    val syncIcon: LiveData<Int> = Transformations.map(_syncStatus) {
        it?.let { status ->
            when (status) {
                SyncStatus.LOADING, SyncStatus.ONLINE -> R.drawable.ic_sync
                SyncStatus.OFFLINE, SyncStatus.CACHE -> R.drawable.ic_offline
            }
        }
    }

    val failure: LiveData<String> = Transformations.map(repository.failure) {
        application.getString(R.string.failure)
    }

    val uvi: LiveData<String> = Transformations.map(weatherForecast) { weather ->
        weather?.let {
            if (it.uvi != Indeterminate) it.uvi.description else ""
        }
    }

    private val _updated = MediatorLiveData<String>()

    val updated: LiveData<String>
        get() {
            return _updated
        }

    private val now = MutableLiveData<Date>()

    init {
        bus.register(this)

        _syncStatus.addSource(weatherForecasts) { weathers ->
            weathers?.let {

                _syncStatus.value = if (DependencyProvider.Current.connected()) {
                    SyncStatus.ONLINE
                } else {
                    SyncStatus.CACHE
                }
            } ?: let {

                _syncStatus.value = if (DependencyProvider.Current.connected()) {
                    SyncStatus.LOADING
                } else {
                    SyncStatus.OFFLINE
                }
            }
        }

        _syncStatus.addSource(failure) {
            _syncStatus.value = SyncStatus.ONLINE
        }

        _updated.addSource(repository.updated) {
            it?.let { date ->
                _updated.value = updated(date, Calendar.getInstance().time)
            }
        }

        _updated.addSource(now) {
            it?.let { end ->
                repository.updated.value?.let { start ->
                    _updated.value = updated(start, end)
                }
            }
        }

        _updated.addSource(_syncStatus) {
            it.offline(
                _updated,
                application.getString(R.string.offline)
            )
        }

        _updated.addSource(repository.failure) {
            it.failure(
                _updated,
                application.getString(R.string.problems_with_sync))
        }

        val conditionTransformation = Transformations.map(weatherForecast) { weather ->
            weather?.let {
                conditionIcon(it.condition)
            }
        }

        _iconCondition.addSource(conditionTransformation) { drawable ->
            drawable?.let {
                _iconCondition.value = it
            }

        }

        _iconCondition.addSource(_syncStatus) {
            it.offline(_iconCondition, R.drawable.ic_condition_offline)
        }

        _iconCondition.addSource(repository.failure) {
            it.failure(_iconCondition, R.drawable.ic_sync_problems)
        }

        setupTimer()
    }

    private fun setupTimer() {
        val timer = Timer()

        val task = object : TimerTask() {
            override fun run() {
                GlobalScope.launch(DependencyProvider.Current.uiDispatcher()) {
                    now.value = Calendar.getInstance().time
                }
            }
        }

        timer.scheduleAtFixedRate(task, UPDATED_INTERVAL, UPDATED_INTERVAL)
    }

    private fun runIfNeverSync(block: () -> Unit) {
        if (weatherForecasts.value.isNullOrEmpty()) {
            block()
        }
    }

    private fun <T> SyncStatus?.offline(liveData: MutableLiveData<T>, value: T) {
        runIfNeverSync {
            this?.let { status ->
                if (status == SyncStatus.OFFLINE) {
                    liveData.value = value
                }
            }
        }
    }

    private fun <T> Exception?.failure(liveData: MutableLiveData<T>, value: T) {
        runIfNeverSync {
            this?.let {
                liveData.value = value
            }
        }
    }

    private fun updated(start: Date, end: Date): String {
        val period = Period(DateTime(start), DateTime(end))

        val prefix = _application.getString(R.string.updated)

        val minutesformatter = PeriodFormatterBuilder()
            .printZeroNever()
            .appendPrefix(prefix).appendMinutes()
            .appendSuffix(_application.getString(R.string.minutes_ago))
            .toFormatter()

        val hoursformatter = PeriodFormatterBuilder()
            .printZeroNever()
            .appendPrefix(prefix).appendHours()
            .appendSuffix(_application.getString(R.string.hours_ago))
            .toFormatter()

        val daysformatter = PeriodFormatterBuilder()
            .printZeroNever()
            .appendPrefix(prefix).appendDays()
            .appendSuffix(_application.getString(R.string.days_ago))
            .toFormatter()

        val weeksformatter = PeriodFormatterBuilder()
            .printZeroNever()
            .appendPrefix(prefix).appendWeeks()
            .appendSuffix(_application.getString(R.string.weeks_ago))
            .toFormatter()

        val monthsFormatter = PeriodFormatterBuilder()
            .printZeroNever()
            .appendPrefix(prefix).appendMonths()
            .appendSuffix(_application.getString(R.string.months_ago))
            .toFormatter()

        val yearsformatter = PeriodFormatterBuilder()
            .printZeroNever()
            .appendPrefix(prefix).appendYears()
            .appendSuffix(_application.getString(R.string.years_ago))
            .toFormatter()

        return when {
            yearsformatter.print(period).isNotEmpty() -> yearsformatter.print(period)
            monthsFormatter.print(period).isNotEmpty() -> monthsFormatter.print(period)
            weeksformatter.print(period).isNotEmpty() -> weeksformatter.print(period)
            daysformatter.print(period).isNotEmpty() -> daysformatter.print(period)
            hoursformatter.print(period).isNotEmpty() -> hoursformatter.print(period)
            minutesformatter.print(period).isNotEmpty() -> minutesformatter.print(period)
            else -> _application.getString(R.string.update_now)
        }
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

    private fun sync(location: Location?) {

        if (!DependencyProvider.Current.connected() && location == null) {
            return
        }

        if (DependencyProvider.Current.connected()) {
            _syncStatus.value = SyncStatus.LOADING
        }

        repository.weather(location)

        bus.post(ChangePageEvent(Page.WEATHER))
    }

    @Subscribe
    fun syncSubscribe(event: SyncEvent) {
        sync(event.location)
    }

    @Subscribe
    fun internetAvaiableSubscribe(event: InternetAvailableEvent) {

        if (event.connected) {

            val status = _syncStatus.value

            _syncStatus.value = when (status) {
                SyncStatus.OFFLINE, SyncStatus.CACHE -> SyncStatus.ONLINE
                else -> _syncStatus.value
            }

            if (status == SyncStatus.OFFLINE) {
                sync()
            }
        } else
            _syncStatus.value = SyncStatus.OFFLINE
    }

    private companion object {
        const val DAY_FORMAT = "dd/MM"
        const val UPDATED_INTERVAL = 60000L
    }
}