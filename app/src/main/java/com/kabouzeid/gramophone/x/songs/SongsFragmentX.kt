package com.kabouzeid.gramophone.x.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import com.kabouzeid.gramophone.x.data.Loading
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.di.ComponentManager
import com.kabouzeid.gramophone.x.isLandscape
import com.kabouzeid.gramophone.x.songs.di.SongsComponent
import com.kabouzeid.gramophone.x.theming.ItemSizeManager
import com.kabouzeid.gramophone.x.theming.getMaxGridItemCount
import com.kabouzeid.gramophone.x.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

sealed class SongsEvents {
    object Reload : SongsEvents()
}

class SongsViewModelX(
        private val repository: ISongsRepository,
        private val channel: Channel<SongsEvents>,
        itemSizeManager: ItemSizeManager
) : ViewModel() {

    private val _songs: MutableLiveData<Resource<List<Song>>> = MutableLiveData()
    val songs: LiveData<Resource<List<Song>>> = _songs

    val size: LiveData<Int> = Transformations.distinctUntilChanged(itemSizeManager.size)

    init {
        viewModelScope.launch {
            channelListener()
        }
    }

    private suspend fun channelListener() {
        channel.consumeEach { Timber.d("event => $it") }
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
}

class SongsViewModelXFactory @Inject constructor(
        private val repository: ISongsRepository,
        private val itemSizeManager: ItemSizeManager,
        private val channel: Channel<SongsEvents>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SongsViewModelX(repository, channel, itemSizeManager) as T
    }
}

class SongsFragmentX : Fragment() {

    private lateinit var component: SongsComponent

    private val vm: SongsViewModelX by lazy {
        ViewModelProviders
                .of(this, component.factory())
                .get(SongsViewModelX::class.java)
    }

    private val listComponent = SongsListComponent()
    private val emptyComponent = SongsEmptyComponent()
    private val errorComponent = SongsErrorComponent()
    private val progressComponent = SongsProgressComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO handle component lifecycle
        component = ComponentManager.appComponent
                .songsComponentBuilder()
                .build()
                .also { ComponentManager.add(it) }

        vm.songs.observe(this, Observer { data ->
            progressComponent.render(data)
            emptyComponent.render(data)
            listComponent.render(data)
            errorComponent.render(data)
        })

        vm.size.observe(this, Observer { size ->
            listComponent.onItemSizeChanged(size)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_songs_x, container, false)

        (view as ViewGroup).run {
            listComponent.inflate(this)
            progressComponent.inflate(this)
            errorComponent.inflate(this)
            emptyComponent.inflate(this)
        }

        vm.load()

        return view
    }

    override fun onDestroy() {
        ComponentManager.remove(component)
        super.onDestroy()
    }

    /* MOVE TO SEPARATE COMPONENT/BASE CLASS */
    fun canUsePalette(): Boolean {
        return false
    }

    fun usePalette(): Boolean {
        return false
    }

    fun getGridSize(): Int {
        return ComponentManager.appComponent.itemSizeManager().get()
    }

    fun getMaxGridSize(): Int {
        return getMaxGridItemCount(isLandscape)
    }
}