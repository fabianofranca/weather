@file:JvmName("BindingAdapters")

package com.fabianofranca.weathercock.views.customs

import android.databinding.BindingAdapter
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v7.widget.AppCompatImageView

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("app:srcCompat")
    fun srcCompat(view: AppCompatImageView, @DrawableRes drawableRes: Int?) {
        drawableRes?.let {
            view.setImageResource(it)
        }
    }

    @JvmStatic
    @BindingAdapter("android:tint")
    fun tint(view: AppCompatImageView, @ColorInt colorInt: Int?) {
        colorInt?.let {
            view.setColorFilter(colorInt)
        }
    }
}