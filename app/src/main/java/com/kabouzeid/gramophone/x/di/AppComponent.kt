package com.kabouzeid.gramophone.x.di

import android.content.Context
import com.kabouzeid.gramophone.x.ordering.SortOrderManager
import com.kabouzeid.gramophone.x.songs.di.SongsComponent
import com.kabouzeid.gramophone.x.theming.ItemSizeManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun songsComponentBuilder(): SongsComponent.Builder

    fun itemSizeManager(): ItemSizeManager

    fun sortOrderManager(): SortOrderManager

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}