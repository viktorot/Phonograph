package com.kabouzeid.gramophone

import android.content.Context
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import com.kabouzeid.gramophone.x.data.Error
import com.kabouzeid.gramophone.x.data.Loading
import com.kabouzeid.gramophone.x.data.Resource
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.runBlocking

@Module
class SongsModule {

    @Provides
    fun providesSongsRepository(context: Context): ISongsRepository {
        return object: ISongsRepository {
            override suspend fun getSongs(): Resource<List<Song>> = runBlocking {
                Loading<List<Song>>()
            }
        }
    }
}