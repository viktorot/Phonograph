package com.kabouzeid.gramophone.x.songs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song

class SongsFragmentAdapterX(private val context: Context) : RecyclerView.Adapter<SongListItemView>() {

    companion object {
        @JvmStatic
        private val TYPE_LIST: Int = 0
        @JvmStatic
        private val TYPE_GRID: Int = 1
    }

    private var viewType: Int = TYPE_LIST
    private var palette: Boolean = false

    private val data: MutableList<Song> = ArrayList()

    fun setData(newData: List<Song>) {
        data.clear()
        data.addAll(newData)

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListItemView {
        val layoutResId = when (viewType) {
            TYPE_GRID -> R.layout.item_grid
            else -> R.layout.item_list
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return SongListItemView(view)
    }

    override fun onBindViewHolder(holder: SongListItemView, position: Int) {
        val song = data[position]

        holder.render(
                data = song,
                separator = holder.adapterPosition == itemCount - 1,
                palette = palette
        )
    }

    override fun getItemCount(): Int = data.count()
}