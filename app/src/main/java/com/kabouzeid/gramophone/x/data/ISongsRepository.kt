package com.kabouzeid.gramophone.x.data

import com.kabouzeid.gramophone.model.Song

interface ISongsRepository {
    suspend fun getSongs(): ArrayList<Song>
}