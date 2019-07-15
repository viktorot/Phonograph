package com.kabouzeid.gramophone.x.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.VisibleForTesting
import androidx.core.widget.ContentLoadingProgressBar
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.Error
import com.kabouzeid.gramophone.x.data.Loading
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.hide
import com.kabouzeid.gramophone.x.show

open class SongsProgressView(container: ViewGroup) {

    private val view: ContentLoadingProgressBar = LayoutInflater
            .from(container.context)
            .inflate(R.layout.view_songs_progress, container, true)
            .findViewById(R.id.progress_bar)

    fun show() {
        view.show()
    }

    fun hide() {
        view.hide()
    }

}

open class SongsProgressComponent {

    @VisibleForTesting
    lateinit var view: SongsProgressView

    @VisibleForTesting
    fun _inflate(container: ViewGroup): SongsProgressView {
        return SongsProgressView(container)
    }

    fun inflate(container: ViewGroup) {
        view = _inflate(container)
    }

    fun render(data: Resource<List<Song>>) {
        when (data is Loading) {
            true -> view.show()
            false -> view.hide()
        }
    }
}