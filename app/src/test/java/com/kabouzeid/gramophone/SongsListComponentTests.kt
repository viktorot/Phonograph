package com.kabouzeid.gramophone

import android.view.ViewGroup
import com.kabouzeid.gramophone.x.bus.EventChannel
import com.kabouzeid.gramophone.x.songs.SongsEvents
import com.kabouzeid.gramophone.x.songs.SongsListComponent
import com.kabouzeid.gramophone.x.songs.SongsListView
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class SongsListComponentTests  {

    private lateinit var component: SongsListComponent

    @Before
    fun setup() {
        component = MockSongsListComponent().apply {
            inflate(mock(), EventChannel())
        }
    }

    @Test
    fun test_displayList() {
        component.onItemSizeChanged(1)

        verify(component.view, times(1)).displayAsList()
        verify(component.view, times(0)).displayAsGrid(1)
        verify(component.view, times(1)).invalidate()
    }

    @Test
    fun test_displayGrid_2() {
        component.onItemSizeChanged(2)

        verify(component.view, times(0)).displayAsList()
        verify(component.view, times(1)).displayAsGrid(2)
        verify(component.view, times(1)).invalidate()
    }

    @Test
    fun test_displayGrid_3() {
        component.onItemSizeChanged(3)

        verify(component.view, times(0)).displayAsList()
        verify(component.view, times(1)).displayAsGrid(3)
        verify(component.view, times(1)).invalidate()
    }

    @Test
    fun test_displayGrid_4() {
        component.onItemSizeChanged(4)

        verify(component.view, times(0)).displayAsList()
        verify(component.view, times(1)).displayAsGrid(4)
        verify(component.view, times(1)).invalidate()
    }

}

class MockSongsListComponent : SongsListComponent() {

    override fun _inflate(container: ViewGroup, channel: EventChannel<SongsEvents>): SongsListView {
        return mock()
    }
}