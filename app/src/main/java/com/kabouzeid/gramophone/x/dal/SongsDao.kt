package com.kabouzeid.gramophone.x.dal

import android.content.Context
import com.kabouzeid.gramophone.loader.SongLoader
import com.kabouzeid.gramophone.model.Song
import java.io.IOException

class SongsDao(private val context: Context) : ISongsDao {

    @Throws(IOException::class)
    override fun get(): List<Song> {
        return SongLoader.getAllSongs(context).toList()
    }
}