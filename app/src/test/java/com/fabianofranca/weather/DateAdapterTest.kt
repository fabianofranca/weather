package com.fabianofranca.weather

import com.fabianofranca.weather.infrastructure.json.DateAdapter
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateAdapterTest {

    private val adapter = DateAdapter()

    @Test
    fun toJson_shouldWork() {
        val date = SimpleDateFormat(
            "EE MMM dd HH:mm:ss zzz yyyy",
            Locale.US
        ).parse("Wed Feb 20 21:56:00 BRT 2019")

        assertEquals(1550710560, adapter.toJson(date))
    }

    @Test
    fun fromJson_shouldWork() {
        val date = adapter.fromJson(1550710560)

        val value = SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", Locale.US).format(date)

        assertEquals("Wed Feb 20 21:56:00 BRT 2019", value)
    }
}