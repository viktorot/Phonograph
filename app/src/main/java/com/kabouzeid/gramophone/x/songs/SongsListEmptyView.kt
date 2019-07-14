package com.kabouzeid.gramophone.x.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.x.hide
import com.kabouzeid.gramophone.x.show

class SongsListEmptyView(@LayoutRes private val layoutResId: Int) {

    private lateinit var container: View

    fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
        val view = inflater.inflate(layoutResId, parent, false)

        container = view.findViewById(R.id.container)

        return view
    }

    fun show() {
        container.show()
    }

    fun hide() {
        container.hide()
    }

}