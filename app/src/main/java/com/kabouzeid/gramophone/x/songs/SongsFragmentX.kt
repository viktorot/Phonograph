package com.kabouzeid.gramophone.x.songs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kabouzeid.gramophone.R
import com.kabouzeid.gramophone.interfaces.PaletteColorHolder
import com.kabouzeid.gramophone.ui.activities.tageditor.AbsTagEditorActivity
import com.kabouzeid.gramophone.ui.activities.tageditor.SongTagEditorActivity
import com.kabouzeid.gramophone.x.data.EventObserver
import com.kabouzeid.gramophone.x.di.ComponentManager
import com.kabouzeid.gramophone.x.isLandscape
import com.kabouzeid.gramophone.x.navigation.Navigator
import com.kabouzeid.gramophone.x.songs.di.SongsComponent
import com.kabouzeid.gramophone.x.theming.getMaxGridItemCount
import javax.inject.Inject

class SongsFragmentX : Fragment() {

    @Inject
    internal lateinit var navigator: Navigator

    private lateinit var component: SongsComponent

    private val vm: SongsViewModelX by lazy {
        ViewModelProviders
                .of(this, component.factory())
                .get(SongsViewModelX::class.java)
    }

    private val listComponent = SongsListComponent()
    private val emptyComponent = SongsEmptyComponent()
    private val errorComponent = SongsErrorComponent()
    private val progressComponent = SongsProgressComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = ComponentManager.appComponent
                .songsComponentBuilder()
                .activity(requireActivity())
                .build()
                .also { ComponentManager.add(it) }

        component.inject(this)

        vm.songs.observe(this, Observer { data ->
            progressComponent.render(data)
            emptyComponent.render(data)
            listComponent.render(data)
            errorComponent.render(data)
        })

        vm.actions.observe(this, EventObserver { event ->
            when (event) {
                is SongsActions.ShowDetails -> {
                    Toast.makeText(requireContext(), "[DETAILS]", Toast.LENGTH_SHORT).show()
                }
                is SongsActions.ShowTagEditor -> {
                    navigator.goToTagEditor(event.songId)
                }
            }
        })

        vm.size.observe(this, Observer { size ->
            listComponent.onItemSizeChanged(size)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_songs_x, container, false)

        (view as ViewGroup).run {
            listComponent.inflate(this, component.channel())
            progressComponent.inflate(this)
            errorComponent.inflate(this)
            emptyComponent.inflate(this)
        }

        vm.load()

        return view
    }

    override fun onDestroy() {
        ComponentManager.remove(component)
        super.onDestroy()
    }

    /* MOVE TO SEPARATE COMPONENT/BASE CLASS */
    fun canUsePalette(): Boolean {
        return false
    }

    fun usePalette(): Boolean {
        return false
    }

    fun getGridSize(): Int {
        return ComponentManager.appComponent.itemSizeManager().get()
    }

    fun getMaxGridSize(): Int {
        return getMaxGridItemCount(isLandscape)
    }
}