package com.fabianofranca.weathercock.entities

sealed class UviType(val start: Float, val description: String, val end: Float? = null) {

    fun validValue(value: Float) = value >= start && (end == null || value <= end)

    companion object {
        fun fromValue(value: Float) = when {
            Low.validValue(value) -> Low
            Moderate.validValue(value) -> Moderate
            High.validValue(value) -> High
            VeryHigh.validValue(value) -> VeryHigh
            Extreme.validValue(value) -> Extreme
            else -> Indeterminate
        }
    }
}

object Low : UviType(0F, "Low", 2.9F)

object Moderate : UviType(3F, "Moderate", 5.9F)

object High : UviType(6F, "High", 7.9F)

object VeryHigh : UviType(8F, "Very High", 10.9F)

object Extreme : UviType(11F, "Extreme")

object Indeterminate : UviType(-1F, "Indeterminate")