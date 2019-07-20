package com.kabouzeid.gramophone.x.songs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.x.bus.EventChannel

class SongsAdapterX(private val context: Context, private val channel: EventChannel<SongsEvents>) : RecyclerView.Adapter<SongsListItemComponent>() {

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

    fun showGridItems() {
        viewType = TYPE_GRID
    }

    fun showListItems() {
        viewType = TYPE_LIST
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsListItemComponent {
        val layoutResId = when (viewType) {
            TYPE_GRID -> R.layout.item_grid
            else -> R.layout.item_list
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return SongsListItemComponent(view, channel).apply {
            inflate()
        }
    }

    override fun onBindViewHolder(holder: SongsListItemComponent, position: Int) {
        holder.data = position
        val song = data[position]

        holder.render(
                data = song,
                separator = holder.adapterPosition == itemCount - 1,
                palette = palette
        )
    }

    override fun getItemCount(): Int = data.count()
}