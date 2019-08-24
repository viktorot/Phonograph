package com.kabouzeid.gramophone.x.songs

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.glide.PhonographColoredTarget
import com.kabouzeid.gramophone.glide.SongGlideRequest
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.util.MusicUtil
import com.kabouzeid.gramophone.x.bus.EventChannel
import com.kabouzeid.gramophone.x.data.Done
import com.kabouzeid.gramophone.x.data.Resource
import com.kabouzeid.gramophone.x.hide
import com.kabouzeid.gramophone.x.show

class SongsListItemView(itemView: View) {

    private val context = itemView.context

    val root: View = itemView.findViewById(R.id.root)

    var image: ImageView? = itemView.findViewById(R.id.image)
    var imageText: TextView? = itemView.findViewById(R.id.image_text)
    var title: TextView? = itemView.findViewById(R.id.title)
    var text: TextView? = itemView.findViewById(R.id.text)
    var menu: View? = itemView.findViewById(R.id.menu)
    var separator: View? = itemView.findViewById(R.id.separator)
    private var shortSeparator: View? = itemView.findViewById(R.id.short_separator)
    var dragView: View? = itemView.findViewById(R.id.drag_view)
    var paletteColorContainer: View? = itemView.findViewById(R.id.palette_color_container)

    lateinit var onClick: () -> Unit
    lateinit var onMenuClick: (id: Int) -> Unit

    init {
        root.setOnClickListener { onClick() }
        menu?.setOnClickListener { buildPopup(it).show() }
    }

    private fun buildPopup(anchor: View): PopupMenu {
        return PopupMenu(context, anchor).apply {
            val popup = inflate(R.menu.menu_item_song)
            setOnMenuItemClickListener { item ->
                this@SongsListItemView.onMenuClick(item.itemId)
                true
            }

            return@apply popup
        }
    }

    fun showSeparator(show: Boolean) {
        shortSeparator?.apply {
            when (show) {
                true -> show()
                false -> hide()
            }
        }
    }

    fun setTitle(text: String) {
        title?.text = text
    }

    fun setSubtitle(text: String) {
        this.text?.text = text
    }

    fun showImage(data: Song, palette: Boolean) {
        if (image == null) return

        SongGlideRequest.Builder.from(Glide.with(context), data)
                .checkIgnoreMediaStore(context)
                .generatePalette(context)
                .build()
                .into(object : PhonographColoredTarget(image) {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        super.onLoadCleared(placeholder)
//                        setColors(defaultFooterColor)
                    }

                    override fun onColorReady(color: Int) {
//                        val c = when (palette) {
//                            true -> color
//                            false -> defaultFooterColor
//                        }
//                        setColors(c)
                    }
                })
    }


}

open class SongsListItemComponent(itemView: View, private val channel: EventChannel<SongsEvents>) : RecyclerView.ViewHolder(itemView) {

    @VisibleForTesting
    lateinit var view: SongsListItemView

    var data: Int = -1


    @VisibleForTesting
    open fun _inflate(): SongsListItemView {
        return SongsListItemView(itemView).apply {
            this.onClick = this@SongsListItemComponent::onClick
            this.onMenuClick = this@SongsListItemComponent::onMenuClick
        }
    }

    fun inflate() {
        view = _inflate()
    }

    fun render(data: Song, separator: Boolean, palette: Boolean) {
        val isChecked = false //isChecked(song)
        //itemView.isActivated = isChecked

        view.showSeparator(separator)
        view.setTitle(data.title)
        view.setSubtitle(MusicUtil.getSongInfoString(data))
        view.showImage(data, palette)

//        loadCover(data, palette)
    }

//    private fun loadCover(data: Song, palette: Boolean) {
//        if (image == null) return
//
//        val ctx = itemView.context
//
//        SongGlideRequest.Builder.from(Glide.with(ctx), data)
//                .checkIgnoreMediaStore(ctx)
//                .generatePalette(ctx).build()
//                .into(object : PhonographColoredTarget(image) {
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                        super.onLoadCleared(placeholder)
//                        setColors(defaultFooterColor)
//                    }
//
//                    override fun onColorReady(color: Int) {
//                        val c = when (palette) {
//                            true -> color
//                            false -> defaultFooterColor
//                        }
//                        setColors(c)
//                    }
//                })
//    }
//
//    private fun setColors(color: Int) {
//        paletteColorContainer?.run {
//            this.setBackgroundColor(color)
//            this@SongsListItemComponent.title?.setTextColor(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color)))
//            this@SongsListItemComponent.text?.setTextColor(MaterialValueHelper.getSecondaryTextColor(context, ColorUtil.isColorLight(color)))
//        }
//    }

    @VisibleForTesting
    fun onClick() {
        channel.send(SongsEvents.Play(data))
    }

    @VisibleForTesting
    fun onMenuClick(@IdRes id: Int) {
        when (id) {
            R.id.action_details -> {
                channel.send(item = SongsEvents.ShowDetails(data))
            }
            R.id.action_tag_editor -> {
                channel.send(item = SongsEvents.ShowTagEditor(data))
            }
            else -> {
            }
        }
    }
}

class SongsListView(container: ViewGroup, channel: EventChannel<SongsEvents>) {

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
    open fun _inflate(container: ViewGroup, channel: EventChannel<SongsEvents>): SongsListView {
        return SongsListView(container, channel)
    }

    fun inflate(container: ViewGroup, channel: EventChannel<SongsEvents>) {
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