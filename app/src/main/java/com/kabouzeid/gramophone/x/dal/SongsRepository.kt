package com.kabouzeid.gramophone.x.dal

import android.content.Context
import com.kabouzeid.gramophone.loader.SongLoader
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Resource
import kotlinx.coroutines.delay
import javax.inject.Inject

class SongsRepository @Inject constructor(private val context: Context) : ISongsRepository {

    override suspend fun getSongs(): Resource<List<Song>> {
        delay(2500)
        return Done(SongLoader.getAllSongs(context))
    }

}