package com.kabouzeid.gramophone.x.data

import android.content.Context
import com.kabouzeid.gramophone.loader.SongLoader
import com.kabouzeid.gramophone.model.Song
import javax.inject.Inject

class SongsRepository @Inject constructor(private val context: Context) : ISongsRepository {

    override suspend fun getSongs(): ArrayList<Song> {
        return SongLoader.getAllSongs(context)
    }

}