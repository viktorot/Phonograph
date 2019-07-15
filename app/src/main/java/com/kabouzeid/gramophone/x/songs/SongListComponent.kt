package com.kabouzeid.gramophone.x.songs

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kabouzeid.appthemehelper.util.ColorUtil
import com.kabouzeid.appthemehelper.util.MaterialValueHelper
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.glide.PhonographColoredTarget
import com.kabouzeid.gramophone.glide.SongGlideRequest
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.util.MusicUtil
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.di.ComponentManager
import com.kabouzeid.gramophone.x.hide
import com.kabouzeid.gramophone.x.isLandscape
import com.kabouzeid.gramophone.x.show
import com.kabouzeid.gramophone.x.theming.showGrid

class SongListItemView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal var image: ImageView? = null
    internal var imageText: TextView? = null
    internal var title: TextView? = null
    internal var text: TextView? = null
    internal var menu: View? = null
    internal var separator: View? = null
    internal var shortSeparator: View? = null
    internal var dragView: View? = null
    internal var paletteColorContainer: View? = null

    private fun buildPopup(anchor: View): PopupMenu {
        return PopupMenu(itemView.context, anchor).apply {
            inflate(R.menu.menu_item_song)
            //setOnMenuItemClickListener {  }
        }
    }

    init {
        image = itemView.findViewById(R.id.image)
        imageText = itemView.findViewById(R.id.image_text)
        title = itemView.findViewById(R.id.title)
        text = itemView.findViewById(R.id.text)
        menu = itemView.findViewById(R.id.menu)
        separator = itemView.findViewById(R.id.separator)
        shortSeparator = itemView.findViewById(R.id.short_separator)
        dragView = itemView.findViewById(R.id.drag_view)
        paletteColorContainer = itemView.findViewById(R.id.palette_color_container)

        menu?.setOnClickListener { buildPopup(it).show() }
    }

    fun render(data: Song, separator: Boolean, palette: Boolean) {
        val isChecked = false //isChecked(song)
        itemView.isActivated = isChecked

        when (separator) {
            true -> shortSeparator?.show()
            false -> shortSeparator?.hide()
        }

        title?.text = data.title
        text?.text = MusicUtil.getSongInfoString(data)

        loadCover(data, palette)
    }

    private fun loadCover(data: Song, palette: Boolean) {
        if (image == null) return

        val ctx = itemView.context

        SongGlideRequest.Builder.from(Glide.with(ctx), data)
                .checkIgnoreMediaStore(ctx)
                .generatePalette(ctx).build()
                .into(object : PhonographColoredTarget(image) {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        super.onLoadCleared(placeholder)
                        setColors(defaultFooterColor)
                    }

                    override fun onColorReady(color: Int) {
                        val c = when (palette) {
                            true -> color
                            false -> defaultFooterColor
                        }
                        setColors(c)
                    }
                })
    }

    private fun setColors(color: Int) {
        paletteColorContainer?.run {
            this.setBackgroundColor(color)
            this@SongListItemView.title?.setTextColor(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color)))
            this@SongListItemView.text?.setTextColor(MaterialValueHelper.getSecondaryTextColor(context, ColorUtil.isColorLight(color)))
        }
    }
}

class SongListView(container: ViewGroup) {

    private val ctx: Context = container.context

    private val view: RecyclerView = LayoutInflater.from(container.context)
            .inflate(R.layout.view_songs_list, container, true)
            .findViewById(R.id.recycler_view)

    private val adapter: SongsAdapterX = SongsAdapterX(container.context)

    init {
        view.adapter = adapter
    }

    fun displayAsGrid(size: Int) {
        view.layoutManager = GridLayoutManager(ctx, size)
        adapter.showGridItems()
    }

    fun displayAsList() {
        view.layoutManager = LinearLayoutManager(ctx)
        adapter.showListItems()
    }

    fun show() {
        view.show()
    }

    fun hide() {
        view.hide()
    }

    fun update(data: List<Song>) {
        adapter.setData(data)
    }

    fun invalidate() {
        adapter.notifyDataSetChanged()
    }
}

class SongListComponent {

    lateinit var view: SongListView

    fun inflate(container: ViewGroup) {
        view = SongListView(container)
    }

    fun render(data: Resource<List<Song>>) {
        if (data !is Done) {
            view.hide()
            return
        }

        when (data.data.isEmpty()) {
            true -> view.hide()
            false -> {
                view.show()
                view.update(data.data)
            }
        }
    }

    fun onItemSizeChanged(size: Int) {
        when (size) {
            1 -> {
                view.displayAsList()
                view.invalidate()
            }
            else -> {
                view.displayAsGrid(size)
                view.invalidate()
            }
        }
    }
}