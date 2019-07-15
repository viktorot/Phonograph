package com.kabouzeid.gramophone

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Error
import com.kabouzeid.gramophone.x.data.Loading
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.songs.SongsFragmentX
import com.kabouzeid.gramophone.x.songs.di.SongsModule
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongsFragmentTests {

    @Test
    fun test_handleLoadingState() {
        SongsModule.fakeRespository = object : ISongsRepository {
            override suspend fun getSongs(): Resource<List<Song>> = runBlocking {
                Loading<List<Song>>()
            }
        }

        val scenario = launchFragmentInContainer<SongsFragmentX>()

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun test_handleErrorState() {
        SongsModule.fakeRespository = object : ISongsRepository {
            override suspend fun getSongs(): Resource<List<Song>> = runBlocking {
                Error<List<Song>>()
            }
        }

        val scenario = launchFragmentInContainer<SongsFragmentX>()

        onView(withText("[ERROR]")).check(matches(isDisplayed()))
    }

    @Test
    fun test_handleEmptyState() {
        SongsModule.fakeRespository = object : ISongsRepository {
            override suspend fun getSongs(): Resource<List<Song>> = runBlocking {
                Done<List<Song>>(emptyList())
            }
        }

        val scenario = launchFragmentInContainer<SongsFragmentX>()

        onView(withId(android.R.id.empty)).check(matches(isDisplayed()))
    }

    @Test
    fun test_handleDataState() {
        SongsModule.fakeRespository = object : ISongsRepository {
            override suspend fun getSongs(): Resource<List<Song>> = runBlocking {
                Done<List<Song>>(listOf(Song.EMPTY_SONG))
            }
        }

        val scenario = launchFragmentInContainer<SongsFragmentX>()

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }
}
