package com.kabouzeid.gramophone.x.dal

import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Error
import com.kabouzeid.gramophone.x.data.Resource
import java.io.IOException
import javax.inject.Inject

class SongsRepository @Inject constructor(private val dao: ISongsDao) : ISongsRepository {

    override suspend fun getSongs(): Resource<List<Song>> {
        return try {
            val data = dao.get()
            Done(data)
        } catch (ex: IOException) {
            Error(ex)
        }
    }

}