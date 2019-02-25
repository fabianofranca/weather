package com.fabianofranca.weathercock.views.home

enum class Page(val position: Int) {
    LOCATION(0),
    WEATHER(1)
}

data class ChangePageEvent(val page: Page)