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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import javax.inject.Inject

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    data class Done<T>(val data: T) : Resource<T>()
    data class Error<T>(val error: Throwable) : Resource<T>()
}

class SongsViewModelX(
        private val repository: ISongsRepository
) : ViewModel() {

    private val _songs: MutableLiveData<Resource<List<Song>>> = MutableLiveData()
    val songs: LiveData<Resource<List<Song>>> = _songs

    fun load() {
        _songs.value = Resource.Loading()

        viewModelScope.launch {
            delay(5000)
            val songs = repository.getSongs()
            _songs.value = Resource.Done(songs.toList())
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

    private val listView = SongListView(this, R.layout.view_songs_list)
    private val emptyView = SongsListEmptyView(R.layout.view_songs_empty)
    private val errorView = SongsListErrorView(R.layout.view_songs_error)
    private val progressView = SongsListEmptyView(R.layout.view_songs_progress)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO handle component lifecycle
        component = DaggerSongsComponent.builder()
                .context(requireContext())
                .build()

        vm.songs.observe(this, Observer { data ->
            when (data) {
                is Resource.Loading -> {
                    progressView.show()
                    emptyView.hide()
                    listView.hide()
                    errorView.hide()
                }
                is Resource.Error -> {
                    progressView.hide()
                    emptyView.hide()
                    listView.hide()
                    errorView.show()
                }
                is Resource.Done -> {
                    progressView.hide()
                    errorView.hide()

                    when (data.data.isEmpty()) {
                        true -> {
                            listView.hide()
                            emptyView.show()
                        }
                        false -> {
                            listView.show()
                            emptyView.hide()
                        }

                    }
                    listView.onDataChanged(data.data)
                }
            }
        })

        App.get(requireContext()).sizeManager.size.observe(this, Observer { size ->
            listView.onItemSizeChanged(size)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_songs_x, container, false)

        (view as ViewGroup).run {
            this.addView(listView.inflate(inflater, this))
            this.addView(emptyView.inflate(inflater, this))
            this.addView(progressView.inflate(inflater, this))
            this.addView(errorView.inflate(inflater, this))
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