package com.fabianofranca.weather.extensions

fun String.fileContent(): String {
    return String.javaClass.classLoader.getResourceAsStream(this).bufferedReader()
        .use { it.readText() }
}