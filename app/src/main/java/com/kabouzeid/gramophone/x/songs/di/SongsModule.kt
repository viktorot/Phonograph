package com.kabouzeid.gramophone.x.songs.di

import android.content.Context
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import com.kabouzeid.gramophone.x.dal.SongsRepository
import dagger.Module
import dagger.Provides

@Module
class SongsModule {

    @Provides
    fun providesSongsRepository(context: Context): ISongsRepository {
        return SongsRepository(context)
    }
}