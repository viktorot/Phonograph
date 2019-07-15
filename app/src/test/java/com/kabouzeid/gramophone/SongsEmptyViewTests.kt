package com.kabouzeid.gramophone

import android.view.ViewGroup
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Error
import com.kabouzeid.gramophone.x.data.Loading
import com.kabouzeid.gramophone.x.songs.SongsEmptyComponent
import com.kabouzeid.gramophone.x.songs.SongsEmptyView
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class SongsEmptyViewTests {

    private lateinit var component: SongsEmptyComponent

    @Before
    fun setup() {
        component = MockSongsEmptyViewComponent().apply {
            inflate(mock(ViewGroup::class.java))
        }
    }

    @Test
    fun test_handleLoadingState() {
        component.render(Loading())
        verify(component.view, times(1)).hide()
        verify(component.view, times(0)).show()
    }

    @Test
    fun test_handleErrorState() {
        component.render(Error())
        verify(component.view, times(1)).hide()
        verify(component.view, times(0)).show()
    }

    @Test
    fun test_handleDoneState_WithData() {
        val data = listOf(
                Song(0, "Title", 0, 2000, 100, "data",
                        1000000L, 1, "Album", 2, "Artist")
        )

        component.render(Done(data))
        verify(component.view, times(1)).hide()
        verify(component.view, times(0)).show()
    }

    @Test
    fun test_handleDoneState_NoData() {
        component.render(Done(emptyList()))
        verify(component.view, times(0)).hide()
        verify(component.view, times(1)).show()
    }

}

class MockSongsEmptyViewComponent : SongsEmptyComponent() {

    override fun _inflate(container: ViewGroup): SongsEmptyView {
        return mock(SongsEmptyView::class.java)
    }
}