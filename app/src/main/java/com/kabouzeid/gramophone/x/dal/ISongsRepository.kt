package com.kabouzeid.gramophone.x.dal

import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.Resource

interface ISongsRepository {
    suspend fun getSongs(): Resource<List<Song>>
}