package com.kabouzeid.gramophone

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.kabouzeid.gramophone.util.PreferenceUtil
import com.kabouzeid.gramophone.utils.MainCoroutineRule
import com.kabouzeid.gramophone.utils.livedata.LiveDataTestUtil
import com.kabouzeid.gramophone.x.bus.EventChannel
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Loading
import com.kabouzeid.gramophone.x.ordering.SortOrderManager
import com.kabouzeid.gramophone.x.songs.SongsViewModelX
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.willReturn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SongsViewModelTests {

    private lateinit var vm: SongsViewModelX

    private val mockRepository = mock<ISongsRepository>()

    private val mockPrefs = mock<PreferenceUtil>().apply {
        given(this.songSortOrder).willReturn { "" }
    }

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        vm = SongsViewModelX(
                repository = mockRepository,
                channel = EventChannel(),
                itemSizeManager = mock(),
                orderManager = SortOrderManager(mockPrefs))
    }

    @Test
    fun `done state should be dispatched if data is successfully loaded`() = runBlockingTest {
        given(mockRepository.getSongs()).willReturn { Done(emptyList()) }

        mainCoroutineRule.pauseDispatcher()

        vm.load()

        mainCoroutineRule.resumeDispatcher()

        assertThat(LiveDataTestUtil.getValue(vm.songs)).isInstanceOf(Done::class.java)
    }

    @Test
    fun `loading state should be dispatched before loading is started`() = runBlockingTest {
        given(mockRepository.getSongs()).willReturn { Done(emptyList()) }

        mainCoroutineRule.pauseDispatcher()

        vm.load()

        assertThat(LiveDataTestUtil.getValue(vm.songs)).isInstanceOf(Loading::class.java)
    }

    @Test
    fun `error state should be dispatched if data loading failed`() = runBlockingTest {
        given(mockRepository.getSongs()).willReturn { com.kabouzeid.gramophone.x.data.Error() }

        mainCoroutineRule.pauseDispatcher()

        vm.load()

        mainCoroutineRule.resumeDispatcher()

        assertThat(LiveDataTestUtil.getValue(vm.songs)).isInstanceOf(com.kabouzeid.gramophone.x.data.Error::class.java)
    }
}