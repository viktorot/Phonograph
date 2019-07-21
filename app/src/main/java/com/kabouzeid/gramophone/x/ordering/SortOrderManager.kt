package com.kabouzeid.gramophone.x.ordering

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kabouzeid.gramophone.util.PreferenceUtil
import javax.inject.Inject

class SortOrderManager @Inject constructor(private val context: Context) {

    private val _order = MutableLiveData<String>()
    val order: LiveData<String> = _order

    init {
        _order.value = PreferenceUtil.getInstance(context).songSortOrder
    }

    fun get(): String {
        return _order.value!!
    }

    fun set(value: String) {
        val old = _order.value
        if (value == old) {
            return
        }

        PreferenceUtil.getInstance(context).songSortOrder = value
        _order.value = value
    }
}