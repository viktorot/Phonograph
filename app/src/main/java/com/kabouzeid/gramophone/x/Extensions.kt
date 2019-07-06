package com.kabouzeid.gramophone.x

import android.view.View
import androidx.fragment.app.Fragment
import com.kabouzeid.gramophone.util.Util

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

val Fragment.isLandscape: Boolean
    get() = Util.isLandscape(resources)
