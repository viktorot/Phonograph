package com.kabouzeid.gramophone.x.di

import android.content.Context
import com.kabouzeid.gramophone.x.songs.di.SongsComponent
import com.kabouzeid.gramophone.x.theming.ItemSizeManager
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton


//@Module(subcomponents = [SongsComponent::class])
//class AppModule {
//
//    @Provides
//    @Singleton
//    fun provideItemSizeManager(context: Context): ItemSizeManager {
//        val mock = Mockito.mock(ItemSizeManager::class.java)
//
//
//
//        return mock
//    }
//
//}