package com.kabouzeid.gramophone.x.songs.di

import com.kabouzeid.gramophone.x.bus.EventChannel
import com.kabouzeid.gramophone.x.di.SubcomponentScope
import com.kabouzeid.gramophone.x.songs.SongsEvents
import com.kabouzeid.gramophone.x.songs.SongsFragmentX
import com.kabouzeid.gramophone.x.songs.SongsViewModelX
import com.kabouzeid.gramophone.x.songs.SongsViewModelXFactory
import dagger.Subcomponent

@SubcomponentScope
@Subcomponent(modules = [SongsModule::class])
interface SongsComponent {

    @Subcomponent.Builder
    abstract class Builder {
        abstract fun build(): SongsComponent
    }

    fun factory(): SongsViewModelXFactory

    fun channel(): EventChannel<SongsEvents>

    fun inject(obj: SongsFragmentX)
    fun inject(obj: SongsViewModelX)
}