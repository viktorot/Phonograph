package com.kabouzeid.gramophone.x.songs.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.bus.EventChannel
import com.kabouzeid.gramophone.x.dal.ISongsDao
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import com.kabouzeid.gramophone.x.dal.SongsDao
import com.kabouzeid.gramophone.x.dal.SongsRepository
import com.kabouzeid.gramophone.x.di.SubcomponentScope
import com.kabouzeid.gramophone.x.songs.navigation.SongsNavigator
import com.kabouzeid.gramophone.x.songs.SongsEvents
import dagger.Module
import dagger.Provides

// appium

@Module
class SongsModule {

    @Provides
    @SubcomponentScope
    fun providesSongsDao(context: Context): ISongsDao {
        return object: ISongsDao {
            override fun get(): List<Song> {
                return emptyList()
//                return (1..5)
//                        .map { Song.BuildMock(it, "title $it", "artist $it", "album $it") }
            }

        }
    }

    @Provides
    @SubcomponentScope
    fun providesSongsRepository(dao: ISongsDao): ISongsRepository {
        return SongsRepository(dao)
    }

    @Provides
    @SubcomponentScope
    fun providesEventChannel(): EventChannel<SongsEvents> {
        return EventChannel()
    }

    @Provides
    @SubcomponentScope
    fun providesNavigator(activity: FragmentActivity): SongsNavigator {
        return SongsNavigator(activity)
    }
}