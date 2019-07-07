package com.kabouzeid.gramophone.x.songs

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.kabouzeid.gramophone.App
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.repo.SongRepo
import kotlinx.coroutines.launch

class SongsViewModelX(app: Application) : AndroidViewModel(app) {

    private val _songs: MutableLiveData<List<Song>> = MutableLiveData()
    val songs: LiveData<List<Song>> = _songs

    fun load() {
        viewModelScope.launch {
            //delay(5000)
            val songs = SongRepo.getSongs(getApplication())
            _songs.value = songs.toList()
        }
    }

}

class SongsFragmentX : Fragment() {

    private lateinit var vm: SongsViewModelX

    private val listView: SongListView = SongListView(this, R.layout.view_songs_list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this).get(SongsViewModelX::class.java)

        vm.songs.observe(this, Observer(listView::onDataChanged))

        App.get(requireContext()).sizeManager.size.observe(this, Observer { size ->
            Log.d("---", "new size => ${size}")
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
        return listView.getMaxGridSize()
    }
}