package com.kabouzeid.gramophone

import android.view.ViewGroup
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Error
import com.kabouzeid.gramophone.x.data.Loading
import com.kabouzeid.gramophone.x.songs.SongsEmptyView
import com.kabouzeid.gramophone.x.songs.SongsEmptyViewComponent
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SongsEmptyViewTests {

    private lateinit var component: SongsEmptyViewComponent

    @Before
    fun setup() {
        component = MockSongsEmptyViewComponent().apply {
            inflate(Mockito.mock(ViewGroup::class.java))
        }
    }

    @Test
    fun test_handleLoadingState() {
        component.render(Loading())
        Mockito.verify(component.view, Mockito.times(1)).hide()
        Mockito.verify(component.view, Mockito.times(0)).show()
    }

    @Test
    fun test_handleErrorState() {
        component.render(Error())
        Mockito.verify(component.view, Mockito.times(1)).hide()
        Mockito.verify(component.view, Mockito.times(0)).show()
    }

    @Test
    fun test_handleDoneState_WithData() {
        val data = listOf(
                Song(0, "Title", 0, 2000, 100, "data",
                        1000000L, 1, "Album", 2, "Artist")
        )

        component.render(Done(data))
        Mockito.verify(component.view, Mockito.times(1)).hide()
        Mockito.verify(component.view, Mockito.times(0)).show()
    }

    @Test
    fun test_handleDoneState_NoData() {
        component.render(Done(emptyList()))
        Mockito.verify(component.view, Mockito.times(0)).hide()
        Mockito.verify(component.view, Mockito.times(1)).show()
    }

}

class MockSongsEmptyViewComponent : SongsEmptyViewComponent() {

    override fun _inflate(container: ViewGroup): SongsEmptyView {
        return Mockito.mock(SongsEmptyView::class.java)
    }
}