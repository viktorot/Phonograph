package com.kabouzeid.gramophone.x.songs.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.bus.EventChannel
import com.kabouzeid.gramophone.x.dal.ISongsDao
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import com.kabouzeid.gramophone.x.songs.SongsEvents
import com.kabouzeid.gramophone.x.songs.navigation.SongsNavigator
import dagger.Module
import dagger.Provides

@Module
class SongsModule {

    companion object {
        lateinit var fakeRepository: ISongsRepository
        var fakeChannel: EventChannel<SongsEvents> = EventChannel()
        var fakeDao = object: ISongsDao {
            override fun get(): List<Song> {
                return emptyList()
            }
        }
    }

    @Provides
    fun providesSongsDao(context: Context): ISongsDao {
        return fakeDao
    }

    @Provides
    fun providesSongsRepository(dao: ISongsDao): ISongsRepository {
        return fakeRepository
    }

    @Provides
    fun providesNavigator(activity: FragmentActivity): SongsNavigator {
        return SongsNavigator(activity)
    }

    @Provides
    fun providesEventChannel(): EventChannel<SongsEvents> {
        return fakeChannel
    }
}