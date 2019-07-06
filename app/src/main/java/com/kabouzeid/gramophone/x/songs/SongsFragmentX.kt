package com.kabouzeid.gramophone.x.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.adapter.song.SongAdapter
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.ui.fragments.mainactivity.library.LibraryFragment
import com.kabouzeid.gramophone.util.PreferenceUtil
import com.kabouzeid.gramophone.x.isLandscape

class SongsFragmentX : Fragment() {

    private lateinit var container: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var empty: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_activity_recycler_view, container, false).run {
            initContainer(this)
            initEmptyView(this)
            initRecyclerView(this)
            this
        }
    }

    private fun initContainer(view: View) {
        container = view.findViewById(R.id.container)
    }

    private fun initEmptyView(view: View) {
        empty = view.findViewById(android.R.id.empty)
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val itemLayoutRes = getItemLayoutRes()

        val parent = parentFragment as LibraryFragment
        val act = requireActivity() as AppCompatActivity

        val data = (1..10).map {
            Song(it, "Title", it, 2000, 100, "data", 1000000L,
                    1, "Album", 2, "Artist")
        }

        recyclerView.adapter = SongAdapter(act, ArrayList(data), itemLayoutRes, false, parent)
    }


    /* MOVE TO SEPARATE COMPONENT/BASE CLASS */
    private var gridSize: Int = 0

    fun getGridSize(): Int {
        if (gridSize == 0) {
            gridSize = when (isLandscape) {
                true -> loadGridSizeLand()
                false -> loadGridSize()
            }
        }
        return gridSize
    }

    fun canUsePalette(): Boolean {
        return false
    }

    fun usePalette(): Boolean {
        return false
    }

    fun loadGridSize(): Int {
        return PreferenceUtil.getInstance(requireActivity()).getSongGridSize(requireActivity())
    }

    fun loadGridSizeLand(): Int {
        return PreferenceUtil.getInstance(requireActivity()).getSongGridSizeLand(requireActivity())
    }

    fun getMaxGridSize(): Int {
        return when (isLandscape) {
            true -> resources.getInteger(R.integer.max_columns_land)
            false -> resources.getInteger(R.integer.max_columns)
        }
    }

    fun getMaxGridSizeForList(): Int {
        return when (isLandscape) {
            true -> resources.getInteger(R.integer.default_list_columns_land)
            false -> resources.getInteger(R.integer.default_list_columns)
        }
    }

    @LayoutRes
    private fun getItemLayoutRes(): Int {
        return if (getGridSize() > getMaxGridSizeForList()) {
            R.layout.item_grid
        } else R.layout.item_list
    }


}