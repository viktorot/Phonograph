package com.kabouzeid.gramophone.x.dal

import com.kabouzeid.gramophone.model.Song
import java.io.IOException

interface ISongsDao {
    @Throws(IOException::class)
    fun get(): List<Song>
}