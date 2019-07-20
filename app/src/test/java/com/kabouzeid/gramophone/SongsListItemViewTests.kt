package com.kabouzeid.gramophone

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
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
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class SongsListItemViewTests {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var component: SongsListItemComponent

    private lateinit var channel: EventChannel<SongsEvents>

    @Before
    fun setup() {
        channel = EventChannel()
        component = MockSongsListItemComponent(Mockito.mock(View::class.java), channel)
    }

    @Test
    fun test_dispatchPlayEvent() = runBlockingTest {
        component.onClick()

        channel.consume(this) { item ->
            Truth.assertThat(item).isInstanceOf(SongsEvents.Play::class.java)
        }

        channel.close()
    }

    @Test
    fun test_dispatchShowDetailsEvent() = runBlockingTest {
        component.onMenuClick(R.id.action_details)

        channel.consume(this) { item ->
            Truth.assertThat(item).isInstanceOf(SongsEvents.ShowDetails::class.java)
        }

        channel.close()
    }
}

class MockSongsListItemComponent(itemView: View, channel: EventChannel<SongsEvents>
) : SongsListItemComponent(itemView, channel) {

    override fun _inflate(): SongsListItemView {
        return Mockito.mock(SongsListItemView::class.java)
    }
}