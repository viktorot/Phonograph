package com.kabouzeid.gramophone.x.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.Error
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.hide
import com.kabouzeid.gramophone.x.show
import kotlinx.coroutines.channels.Channel

open class SongsErrorView(container: ViewGroup) {

    private val view: View = LayoutInflater
            .from(container.context)
            .inflate(R.layout.view_songs_error, container, true)
            .findViewById(R.id.error)

    fun show() {
        view.show()
    }

    fun hide() {
        view.hide()
    }
}

open class SongsErrorComponent {

    @VisibleForTesting
    lateinit var view: SongsErrorView

    @VisibleForTesting
    open fun _inflate(container: ViewGroup): SongsErrorView {
        return SongsErrorView(container)
    }

    fun inflate(container: ViewGroup) {
        view = _inflate(container)
    }

    fun render(data: Resource<List<Song>>) {
        when (data) {
            is Error -> view.show()
            else -> view.hide()
        }
    }
}