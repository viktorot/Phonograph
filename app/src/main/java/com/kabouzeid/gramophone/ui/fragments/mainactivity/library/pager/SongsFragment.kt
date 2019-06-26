package com.kabouzeid.gramophone.ui.fragments.mainactivity.library.pager

import android.content.Context
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager

import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.adapter.song.ShuffleButtonSongAdapter
import com.kabouzeid.gramophone.adapter.song.SongAdapter
import com.kabouzeid.gramophone.interfaces.LoaderIds
import com.kabouzeid.gramophone.loader.SongLoader
import com.kabouzeid.gramophone.misc.WrappedAsyncTaskLoader
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.util.PreferenceUtil

import java.util.ArrayList

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
class SongsFragment : AbsLibraryPagerRecyclerViewCustomGridSizeFragment<SongAdapter, GridLayoutManager>()
        , LoaderManager.LoaderCallbacks<ArrayList<Song>> {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(LOADER_ID, null, this)
    }

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(activity, gridSize)
    }

    override fun createAdapter(): SongAdapter {
        val itemLayoutRes = itemLayoutRes
        notifyLayoutResChanged(itemLayoutRes)
        val usePalette = loadUsePalette()
        val dataSet = if (adapter == null) ArrayList() else adapter.dataSet

        return if (gridSize <= maxGridSizeForList) {
            ShuffleButtonSongAdapter(
                    libraryFragment.mainActivity,
                    dataSet,
                    itemLayoutRes,
                    usePalette,
                    libraryFragment)
        } else SongAdapter(
                libraryFragment.mainActivity,
                dataSet,
                itemLayoutRes,
                usePalette,
                libraryFragment)
    }

    override fun getEmptyMessage(): Int {
        return R.string.no_songs
    }

    override fun onMediaStoreChanged() {
        loaderManager.restartLoader(LOADER_ID, null, this)
    }

    override fun loadSortOrder(): String {
        return PreferenceUtil.getInstance(requireActivity()).songSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.getInstance(requireActivity()).songSortOrder = sortOrder
    }

    override fun setSortOrder(sortOrder: String) {
        loaderManager.restartLoader(LOADER_ID, null, this)
    }

    override fun loadGridSize(): Int {
        return PreferenceUtil.getInstance(requireActivity()).getSongGridSize(requireActivity())
    }

    override fun saveGridSize(gridSize: Int) {
        PreferenceUtil.getInstance(requireActivity()).setSongGridSize(gridSize)
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.getInstance(requireActivity()).getSongGridSizeLand(requireActivity())
    }

    override fun saveGridSizeLand(gridSize: Int) {
        PreferenceUtil.getInstance(requireActivity()).setSongGridSizeLand(gridSize)
    }

    public override fun saveUsePalette(usePalette: Boolean) {
        PreferenceUtil.getInstance(requireActivity()).setSongColoredFooters(usePalette)
    }

    public override fun loadUsePalette(): Boolean {
        return PreferenceUtil.getInstance(requireActivity()).songColoredFooters()
    }

    public override fun setUsePalette(usePalette: Boolean) {
        adapter.usePalette(usePalette)
    }

    override fun setGridSize(gridSize: Int) {
        layoutManager.spanCount = gridSize
        adapter.notifyDataSetChanged()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Song>> {
        return AsyncSongLoader(requireContext())
    }

    override fun onLoadFinished(loader: Loader<ArrayList<Song>>, data: ArrayList<Song>) {
        adapter.swapDataSet(data)
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Song>>) {
        adapter.swapDataSet(ArrayList())
    }

    private class AsyncSongLoader(context: Context) : WrappedAsyncTaskLoader<ArrayList<Song>>(context) {

        override fun loadInBackground(): ArrayList<Song>? {
            return SongLoader.getAllSongs(context)
        }
    }

    companion object {

        private val LOADER_ID = LoaderIds.SONGS_FRAGMENT
    }
}
