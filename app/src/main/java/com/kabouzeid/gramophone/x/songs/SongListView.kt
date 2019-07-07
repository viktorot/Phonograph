package com.kabouzeid.gramophone.x.songs

import android.content.Context
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
import com.kabouzeid.gramophone.x.hide
import com.kabouzeid.gramophone.x.isLandscape
import com.kabouzeid.gramophone.x.show

class SongListView(
        private val fragment: Fragment,
        @LayoutRes private val layoutResId: Int
) {

    private var gridSize: Int = 0

    private lateinit var container: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var empty: TextView

    fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
        val view = inflater.inflate(layoutResId, parent, false)
        initContainer(view)
        initEmptyView(view)
        initRecyclerView(view)

        return view
    }

    fun onDataChanged(data: List<Song>) {
        (recyclerView.adapter as SongAdapter).swapDataSet(ArrayList(data))

        when (data.isEmpty()) {
            true -> {
                recyclerView.hide()
                empty.show()
            }
            false -> {
                recyclerView.show()
                empty.hide()
            }
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
        recyclerView.layoutManager = LinearLayoutManager(fragment.requireContext())

        val itemLayoutRes = getItemLayoutRes()

        val parent = fragment.parentFragment as LibraryFragment
        val act = fragment.requireActivity() as AppCompatActivity

//        val data = (1..10).map {
//            Song(it, "Title", it, 2000, 100, "data", 1000000L,
//                    1, "Album", 2, "Artist")
//        }

        recyclerView.adapter = SongAdapter(act, ArrayList(), itemLayoutRes, false, parent)
    }

    fun getMaxGridSize(): Int {
        return when (fragment.isLandscape) {
            true -> fragment.resources.getInteger(R.integer.max_columns_land)
            false -> fragment.resources.getInteger(R.integer.max_columns)
        }
    }

    fun getMaxGridSizeForList(): Int {
        return when (fragment.isLandscape) {
            true -> fragment.resources.getInteger(R.integer.default_list_columns_land)
            false -> fragment.resources.getInteger(R.integer.default_list_columns)
        }
    }

    fun loadGridSize(): Int {
        val ctx = fragment.requireContext()
        return PreferenceUtil.getInstance(ctx).getSongGridSize(ctx)
    }

    fun loadGridSizeLand(): Int {
        val ctx = fragment.requireContext()
        return PreferenceUtil.getInstance(ctx).getSongGridSizeLand(ctx)
    }

    fun getGridSize(): Int {
        if (gridSize == 0) {
            gridSize = when (fragment.isLandscape) {
                true -> loadGridSizeLand()
                false -> loadGridSize()
            }
        }
        return gridSize
    }

    @LayoutRes
    private fun getItemLayoutRes(): Int {
        return if (getGridSize() > getMaxGridSizeForList()) {
            R.layout.item_grid
        } else R.layout.item_list
    }
}