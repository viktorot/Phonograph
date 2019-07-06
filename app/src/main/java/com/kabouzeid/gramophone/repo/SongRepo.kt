package com.kabouzeid.gramophone.repo

import android.content.Context
import com.kabouzeid.gramophone.loader.SongLoader
import com.kabouzeid.gramophone.model.Song

object SongRepo {

    suspend fun getSongs(context: Context): ArrayList<Song> {
        return SongLoader.getAllSongs(context)
    }

}