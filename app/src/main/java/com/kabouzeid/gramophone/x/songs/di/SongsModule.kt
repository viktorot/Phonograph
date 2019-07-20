package com.kabouzeid.gramophone.x.songs.di

import android.content.Context
import com.kabouzeid.gramophone.x.bus.EventChannel
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import com.kabouzeid.gramophone.x.dal.SongsRepository
import com.kabouzeid.gramophone.x.di.SubcomponentScope
import com.kabouzeid.gramophone.x.songs.SongsEvents
import dagger.Module
import dagger.Provides

@Module
class SongsModule {

    @Provides
    @SubcomponentScope
    fun providesSongsRepository(context: Context): ISongsRepository {
        return SongsRepository(context)
    }

    @Provides
    @SubcomponentScope
    fun providesEventChannel(): EventChannel<SongsEvents> {
        return EventChannel()
    }
}