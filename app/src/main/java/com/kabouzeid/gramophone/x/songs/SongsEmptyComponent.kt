package com.kabouzeid.gramophone.x.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.hide
import com.kabouzeid.gramophone.x.show

open class SongsEmptyView(container: ViewGroup) {

    private val view: View = LayoutInflater
            .from(container.context)
            .inflate(R.layout.view_songs_empty, container, true)
            .findViewById(android.R.id.empty)

    fun show() {
        view.show()
    }

    fun hide() {
        view.hide()
    }
}

open class SongsEmptyComponent {

    @VisibleForTesting
    lateinit var view: SongsEmptyView

    @VisibleForTesting
    open fun _inflate(container: ViewGroup): SongsEmptyView {
        return SongsEmptyView(container)
    }

    fun inflate(container: ViewGroup) {
        view = _inflate(container)
    }

    fun render(data: Resource<List<Song>>) {
        if (data !is Done) {
            view.hide()
            return
        }

        when (data.data.isEmpty()) {
            true -> view.show()
            false -> view.hide()
        }
    }
}