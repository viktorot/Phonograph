package com.kabouzeid.gramophone.x.songs.di

import android.content.Context
import androidx.fragment.app.Fragment
import com.kabouzeid.gramophone.x.songs.SongsFragmentX
import com.kabouzeid.gramophone.x.songs.SongsViewModelXFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SongsModule::class])
interface SongsComponent {

    @Component.Builder
    abstract class Builder {

        @BindsInstance
        abstract fun context(context: Context): Builder

        abstract fun build(): SongsComponent
    }

    fun factory(): SongsViewModelXFactory

    fun inject(obj: SongsFragmentX)
}