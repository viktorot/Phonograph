package com.kabouzeid.gramophone.x.theming

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kabouzeid.gramophone.util.PreferenceUtil

class ItemSizeManager(private val context: Context) {

    private val _size = MutableLiveData<Int>()
    val size: LiveData<Int> = _size

    init {
        get()
    }

    fun get(landscape: Boolean = false) {
        val storedSize = when (landscape) {
            true -> PreferenceUtil.getInstance(context).getSongGridSizeLand(context)
            false -> PreferenceUtil.getInstance(context).getSongGridSize(context)
        }

        _size.value = storedSize
    }

    fun set(value: Int, landscape: Boolean = false) {
        val old = _size.value
        if (old == value) {
            return
        }

        store(value, landscape)

        _size.value = value
    }

    private fun store(value: Int, landscape: Boolean) {
        when (landscape) {
            true -> PreferenceUtil.getInstance(context).setSongGridSizeLand(value)
            false -> PreferenceUtil.getInstance(context).setSongGridSize(value)
        }
    }

}