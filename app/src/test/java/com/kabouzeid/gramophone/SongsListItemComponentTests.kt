package com.kabouzeid.gramophone

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.utils.MainCoroutineRule
import com.kabouzeid.gramophone.x.bus.EventChannel
import com.kabouzeid.gramophone.x.songs.SongsEvents
import com.kabouzeid.gramophone.x.songs.SongsListItemComponent
import com.kabouzeid.gramophone.x.songs.SongsListItemView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class SongsListItemComponentTests {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var component: SongsListItemComponent

    private lateinit var channel: EventChannel<SongsEvents>

    @Before
    fun setup() {
        channel = EventChannel()
        component = MockSongsListItemComponent(mock(View::class.java), channel).apply {
            inflate()
        }
    }

    @Test
    fun `should dispatch Play event`() = runBlockingTest {
        component.onClick()

        channel.consume(this) { item ->
            assertThat(item).isInstanceOf(SongsEvents.Play::class.java)
        }

        channel.close()
    }

    @Test
    fun `should dispatch ShowDetails event`() = runBlockingTest {
        component.onMenuClick(R.id.action_details)

        channel.consume(this) { item ->
            assertThat(item).isInstanceOf(SongsEvents.ShowDetails::class.java)
        }

        channel.close()
    }

    @Test
    fun `set model data to view`() {
        val data = Song(0, "Title", 0, 2000, 100, "data",
                1000000L, 1, "Album", 2, "Artist")

        component.render(data, true, false)

        verify(component.view, times(1)).setTitle("Title")
        verify(component.view, times(1)).setSubtitle("Artist  •  Album")
    }
}

class MockSongsListItemComponent(itemView: View, channel: EventChannel<SongsEvents>
) : SongsListItemComponent(itemView, channel) {

    override fun _inflate(): SongsListItemView {
        return mock(SongsListItemView::class.java)
    }
}