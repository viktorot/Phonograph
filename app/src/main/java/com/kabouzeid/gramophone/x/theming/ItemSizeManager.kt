package com.kabouzeid.gramophone.x.theming

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kabouzeid.gramophone.util.PreferenceUtil
import java.lang.IllegalStateException

class ItemSizeManager(private val context: Context) {

    private val _size = MutableLiveData<Int>()
    val size: LiveData<Int> = _size

    private var isLandscape = false

    private var inited: Boolean = false

    fun init(landscape: Boolean) {
        inited = true

        isLandscape = landscape

        val storedSize = when (isLandscape) {
            true -> PreferenceUtil.getInstance(context).getSongGridSizeLand(context)
            false -> PreferenceUtil.getInstance(context).getSongGridSize(context)
        }

        _size.value = storedSize
    }

    fun get(): Int {
        verifyInitialized()

        return _size.value!!
    }

    fun set(value: Int) {
        verifyInitialized()

        val old = _size.value
        if (old == value) {
            return
        }

        store(value)

        _size.value = value
    }

    private fun store(value: Int) {
        verifyInitialized()

        when (isLandscape) {
            true -> PreferenceUtil.getInstance(context).setSongGridSizeLand(value)
            false -> PreferenceUtil.getInstance(context).setSongGridSize(value)
        }
    }

    private fun verifyInitialized() {
        if (!inited) {
            throw IllegalStateException("Not initialized")
        }
    }

}