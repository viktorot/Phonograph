package com.kabouzeid.gramophone.x.songs

import androidx.lifecycle.*
import com.kabouzeid.gramophone.helper.MusicPlayerRemote
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.bus.EventChannel
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Event
import com.kabouzeid.gramophone.x.data.Loading
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.ordering.SortOrderManager
import com.kabouzeid.gramophone.x.theming.ItemSizeManager
import com.kabouzeid.gramophone.x.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class SongsEvents {
    data class Play(val position: Int) : SongsEvents()
    data class ShowDetails(val position: Int) : SongsEvents()
    data class ShowTagEditor(val position: Int) : SongsEvents()
}

sealed class SongsActions {
    data class ShowDetails(val song: Song): SongsActions()
    data class ShowTagEditor(val song: Song): SongsActions()
}

class SongsViewModelX(
        private val repository: ISongsRepository,
        private val channel: EventChannel<SongsEvents>,
        private val orderManager: SortOrderManager,
        itemSizeManager: ItemSizeManager
) : ViewModel() {

    private val orderObserver = Observer(this::onOrderChanged)

    private val _songs = MutableLiveData<Resource<List<Song>>>()
    val songs: LiveData<Resource<List<Song>>> = _songs

    private val _actions = MutableLiveData<Event<SongsActions>>()
    val actions: LiveData<Event<SongsActions>> = _actions

    val size: LiveData<Int> = Transformations.distinctUntilChanged(itemSizeManager.size)

    init {
        channelListener()
        orderManager.order.observeForever(orderObserver)
    }

    private fun channelListener() {
        channel.consume(viewModelScope) { event ->
            when (event) {
                is SongsEvents.Play -> {
                    val data = ArrayList((_songs.value as Done).data)
                    MusicPlayerRemote.openQueue(data, event.position, true)
                }
                is SongsEvents.ShowDetails -> {
                    val song = (_songs.value as Done).data[event.position]
                    _actions.postValue(Event(SongsActions.ShowDetails(song)))
                }
                is SongsEvents.ShowTagEditor -> {
                    val song = (_songs.value as Done).data[event.position]
                    _actions.postValue(Event(SongsActions.ShowTagEditor(song)))
                }
            }
        }
    }

    private fun onOrderChanged(order: String) {
    }

    fun load() = wrapEspressoIdlingResource {
        _songs.value = Loading()

        viewModelScope.launch(Dispatchers.Default) {
            val result = repository.getSongs()
            withContext(Dispatchers.Main) {
                _songs.value = result
            }
        }
    }

    override fun onCleared() {
        orderManager.order.removeObserver(orderObserver)
    }
}

class SongsViewModelXFactory @Inject constructor(
        private val repository: ISongsRepository,
        private val itemSizeManager: ItemSizeManager,
        private val orderManager: SortOrderManager,
        private val channel: EventChannel<SongsEvents>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SongsViewModelX(repository, channel, orderManager, itemSizeManager) as T
    }
}