package com.kabouzeid.gramophone.x.songs

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
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.kabouzeid.appthemehelper.util.ColorUtil
import com.kabouzeid.appthemehelper.util.MaterialValueHelper
import com.kabouzeid.gramophone.App
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.glide.PhonographColoredTarget
import com.kabouzeid.gramophone.glide.SongGlideRequest
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.util.MusicUtil
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
        ButterKnife.bind(this, itemView)

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
            false ->shortSeparator?.hide()
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

        container = view.findViewById(R.id.container)
        empty = view.findViewById(android.R.id.empty)
        recyclerView = view.findViewById(R.id.recycler_view)

        initLayoutManager()
        initAdapter()

        return view
    }

    fun onDataChanged(data: List<Song>) {
        (recyclerView.adapter as SongsFragmentAdapterX).setData(data)

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


    fun onItemSizeChanged(size: Int) {
        gridSize = size
        initLayoutManager()
    }

    private fun initAdapter() {
//        val itemLayoutRes = getItemLayoutRes()
//
//        val parent = fragment.parentFragment as LibraryFragment
//        val act = fragment.requireActivity() as AppCompatActivity

//        val data = (1..10).map {
//            Song(it, "Title", it, 2000, 100, "data", 1000000L,
//                    1, "Album", 2, "Artist")
//        }

        //recyclerView.adapter = SongAdapter(act, ArrayList(), itemLayoutRes, false, parent)
        recyclerView.adapter = SongsFragmentAdapterX(fragment.requireContext())
    }

    private fun initLayoutManager() {
        if (gridSize == 0) {
            return
        }

        recyclerView.layoutManager = GridLayoutManager(fragment.requireContext(), gridSize)
    }

    fun loadGridSize(): Int {
        return App.get(fragment.requireContext()).sizeManager.get()
    }

    fun loadGridSizeLand(): Int {
        return App.get(fragment.requireContext()).sizeManager.get()
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
        return when (showGrid(gridSize)) {
            true -> R.layout.item_grid
            false -> R.layout.item_list
        }
    }
}