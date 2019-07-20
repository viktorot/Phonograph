package com.kabouzeid.gramophone.x.songs

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.VisibleForTesting
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
import com.kabouzeid.gramophone.x.hide
import com.kabouzeid.gramophone.x.show
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import timber.log.Timber

class SongsListItemView(itemView: View, private val channel: Channel<SongsEvents>) : RecyclerView.ViewHolder(itemView) {

    var data: Int = -1

    private val root: View = itemView.findViewById(R.id.root)
    private var image: ImageView? = itemView.findViewById(R.id.image)
    private var imageText: TextView? = itemView.findViewById(R.id.image_text)
    private var title: TextView? = itemView.findViewById(R.id.title)
    private var text: TextView? = itemView.findViewById(R.id.text)
    private var menu: View? = itemView.findViewById(R.id.menu)
    private var separator: View? = itemView.findViewById(R.id.separator)
    private var shortSeparator: View? = itemView.findViewById(R.id.short_separator)
    private var dragView: View? = itemView.findViewById(R.id.drag_view)
    private var paletteColorContainer: View? = itemView.findViewById(R.id.palette_color_container)

    private fun buildPopup(anchor: View): PopupMenu {
        return PopupMenu(itemView.context, anchor).apply {
            inflate(R.menu.menu_item_song)
            //setOnMenuItemClickListener {  }
        }
    }

    init {
        root.setOnClickListener {
            GlobalScope.launch { channel.send(SongsEvents.Play(data)) }
        }
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
            this@SongsListItemView.title?.setTextColor(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color)))
            this@SongsListItemView.text?.setTextColor(MaterialValueHelper.getSecondaryTextColor(context, ColorUtil.isColorLight(color)))
        }
    }
}

class SongsListView(container: ViewGroup, channel: Channel<SongsEvents>) {

    private val ctx: Context = container.context

    private val view: RecyclerView = LayoutInflater.from(container.context)
            .inflate(R.layout.view_songs_list, container, true)
            .findViewById(R.id.recycler_view)

    private val adapter: SongsAdapterX = SongsAdapterX(container.context, channel)

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

open class SongsListComponent {

    @VisibleForTesting
    lateinit var view: SongsListView

    @VisibleForTesting
    open fun _inflate(container: ViewGroup, channel: Channel<SongsEvents>): SongsListView {
        return SongsListView(container, channel)
    }

    fun inflate(container: ViewGroup, channel: Channel<SongsEvents>) {
        view = _inflate(container, channel)
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