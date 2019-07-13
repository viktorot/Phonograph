package com.kabouzeid.gramophone.x.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.kabouzeid.gramophone.App
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.data.ISongsRepository
import com.kabouzeid.gramophone.x.isLandscape
import com.kabouzeid.gramophone.x.songs.di.DaggerSongsComponent
import com.kabouzeid.gramophone.x.songs.di.SongsComponent
import com.kabouzeid.gramophone.x.theming.getMaxGridItemCount
import kotlinx.coroutines.launch
import javax.inject.Inject

class SongsViewModelX(
        private val repository: ISongsRepository
) : ViewModel() {

    private val _songs: MutableLiveData<List<Song>> = MutableLiveData()
    val songs: LiveData<List<Song>> = _songs

    fun load() {
        viewModelScope.launch {
//            delay(5000)
            val songs = repository.getSongs()
            _songs.value = songs.toList()
        }
    }
}

class SongsViewModelXFactory @Inject constructor(
        private val repository: ISongsRepository)
    : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SongsViewModelX(repository) as T
    }
}

class SongsFragmentX : Fragment() {

    private lateinit var component: SongsComponent

    private val vm: SongsViewModelX by lazy {
        ViewModelProviders
                .of(this, component.factory())
                .get(SongsViewModelX::class.java)
    }

    private val listView: SongListView = SongListView(this, R.layout.view_songs_list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO handle component lifecycle
        component = DaggerSongsComponent.builder()
                .context(requireContext())
                .build()

        vm.songs.observe(this, Observer { data ->
            listView.onDataChanged(data)
        })

        App.get(requireContext()).sizeManager.size.observe(this, Observer { size ->
            listView.onItemSizeChanged(size)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_songs_x, container, false)

        (view as ViewGroup).run {
            this.addView(listView.inflate(inflater, this))
        }

        vm.load()

        return view
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