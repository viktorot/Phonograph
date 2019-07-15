package com.kabouzeid.gramophone.x.songs.di

import android.content.Context
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import dagger.Module
import dagger.Provides

@Module
class SongsModule {

    companion object {
        lateinit var fakeRespository: ISongsRepository
    }

    @Provides
    fun providesSongsRepository(context: Context): ISongsRepository {
        return fakeRespository
    }
}
