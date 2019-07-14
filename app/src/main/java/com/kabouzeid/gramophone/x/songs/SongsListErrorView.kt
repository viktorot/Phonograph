package com.kabouzeid.gramophone.x.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Error
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.hide
import com.kabouzeid.gramophone.x.show

class SongsListErrorView(@LayoutRes private val layoutResId: Int) {

    private lateinit var container: View

    fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
        val view = inflater.inflate(layoutResId, parent, false)

        container = view.findViewById(R.id.container)

        return view
    }

    fun render(data: Resource<List<Song>>) {
        when (data is Error) {
            true -> show()
            false -> hide()
        }
    }

    private fun show() {
        container.show()
    }

    private fun hide() {
        container.hide()
    }

}