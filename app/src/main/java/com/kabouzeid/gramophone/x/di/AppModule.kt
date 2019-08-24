package com.kabouzeid.gramophone.x.di

import android.content.Context
import com.kabouzeid.gramophone.util.PreferenceUtil
import com.kabouzeid.gramophone.x.ordering.SortOrderManager
import com.kabouzeid.gramophone.x.songs.di.SongsComponent
import com.kabouzeid.gramophone.x.theming.ItemSizeManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [SongsComponent::class])
class AppModule {

    @Provides
    @Singleton
    fun provideItemSizeManager(context: Context): ItemSizeManager {
        return ItemSizeManager(context)
    }

    @Provides
    @Singleton
    fun provideSortOrderManager(context: Context): SortOrderManager {
        return SortOrderManager(PreferenceUtil.getInstance(context))
    }

}