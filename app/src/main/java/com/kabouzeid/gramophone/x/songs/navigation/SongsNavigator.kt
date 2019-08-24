package com.kabouzeid.gramophone.x.songs.navigation

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.kabouzeid.gramophone.dialogs.SongDetailDialog
import com.kabouzeid.gramophone.model.Song
import com.kabouzeid.gramophone.ui.activities.tageditor.AbsTagEditorActivity
import com.kabouzeid.gramophone.ui.activities.tageditor.SongTagEditorActivity

open class SongsNavigator(private var activity: FragmentActivity) {

    fun goToDetails(song: Song) {
        SongDetailDialog.create(song).show(activity.supportFragmentManager, "SONG_DETAILS")
    }

    fun goToTagEditor(song: Song) {
        val intent = Intent(activity, SongTagEditorActivity::class.java).apply {
            putExtra(AbsTagEditorActivity.EXTRA_ID, song.id)
        }
        activity.startActivity(intent)
    }

}