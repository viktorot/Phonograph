package com.kabouzeid.gramophone.x.songs.di

import com.kabouzeid.gramophone.x.di.AppComponent
import com.kabouzeid.gramophone.x.di.SubcomponentScope
import com.kabouzeid.gramophone.x.songs.SongsFragmentX
import com.kabouzeid.gramophone.x.songs.SongsViewModelXFactory
import dagger.BindsInstance
import dagger.Subcomponent

@SubcomponentScope
@Subcomponent(modules = [SongsModule::class])
interface SongsComponent {

    @Subcomponent.Builder
    abstract class Builder {
        abstract fun build(): SongsComponent
    }

    fun factory(): SongsViewModelXFactory

    fun inject(obj: SongsFragmentX)
}