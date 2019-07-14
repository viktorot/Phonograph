package com.kabouzeid.gramophone.x.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.widget.ContentLoadingProgressBar
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.Error
import com.kabouzeid.gramophone.x.data.Loading
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.hide
import com.kabouzeid.gramophone.x.show

class SongsListProgressView(@LayoutRes private val layoutResId: Int) {

    private lateinit var progress: ContentLoadingProgressBar

    fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
        val view = inflater.inflate(layoutResId, parent, false)

        progress = view.findViewById(R.id.progress_bar)

        return view
    }

    fun render(data: Resource<List<Song>>) {
        when (data is Loading) {
            true -> show()
            false -> hide()
        }
    }

    private fun show() {
        progress.show()
    }

    private fun hide() {
        progress.hide()
    }
}