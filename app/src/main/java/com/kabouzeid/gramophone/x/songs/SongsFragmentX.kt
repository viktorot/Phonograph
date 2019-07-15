package com.kabouzeid.gramophone.x.songs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.dal.ISongsRepository
import com.kabouzeid.gramophone.x.data.Error
import com.kabouzeid.gramophone.x.data.Loading
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.di.ComponentManager
import com.kabouzeid.gramophone.x.isLandscape
import com.kabouzeid.gramophone.x.songs.di.SongsComponent
import com.kabouzeid.gramophone.x.songs.di.SongsModule
import com.kabouzeid.gramophone.x.theming.ItemSizeManager
import com.kabouzeid.gramophone.x.theming.getMaxGridItemCount
import com.kabouzeid.gramophone.x.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import javax.inject.Inject

class SongsViewModelX(
        private val repository: ISongsRepository,
        private val itemSizeManager: ItemSizeManager
) : ViewModel() {

    private val _songs: MutableLiveData<Resource<List<Song>>> = MutableLiveData()
    val songs: LiveData<Resource<List<Song>>> = _songs

    val size: LiveData<Int> = itemSizeManager.size

    fun load() {
        wrapEspressoIdlingResource {
            _songs.value = Loading()

            viewModelScope.launch {
                _songs.value = repository.getSongs()
            }
        }
    }
}

class SongsViewModelXFactory @Inject constructor(
        private val repository: ISongsRepository,
        private val itemSizeManager: ItemSizeManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SongsViewModelX(repository, itemSizeManager) as T
    }
}

class SongsFragmentX : Fragment() {

    private lateinit var component: SongsComponent

    private val vm: SongsViewModelX by lazy {
        ViewModelProviders
                .of(this, component.factory())
                .get(SongsViewModelX::class.java)
    }

    private val listView = SongListView(this, R.layout.view_songs_list)
    private val emptyView = SongsEmptyViewComponent()
    private val errorView = SongsListErrorView(R.layout.view_songs_error)
    private val progressView = SongsListProgressView(R.layout.view_songs_progress)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO handle component lifecycle
        component = ComponentManager.appComponent
                .songsComponentBuilder()
                .build()
                .also { ComponentManager.add(it) }

        vm.songs.observe(this, Observer { data ->
            progressView.render(data)
            emptyView.render(data)
            listView.render(data)
            errorView.render(data)
        })

        vm.size.observe(this, Observer { size ->
            listView.onItemSizeChanged(size)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_songs_x, container, false)

        (view as ViewGroup).run {
            this.addView(listView.inflate(inflater, this))
            this.addView(progressView.inflate(inflater, this))
            this.addView(errorView.inflate(inflater, this))

            emptyView.inflate(this)
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
        return listView.getGridSize()
    }

    fun getMaxGridSize(): Int {
        return getMaxGridItemCount(isLandscape)
    }
}