package com.kabouzeid.gramophone.x.navigation

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.kabouzeid.gramophone.ui.activities.tageditor.AbsTagEditorActivity
import com.kabouzeid.gramophone.ui.activities.tageditor.SongTagEditorActivity

open class Navigator(private var activity: FragmentActivity) {

    fun goToTagEditor(songId: Int) {
        val intent = Intent(activity, SongTagEditorActivity::class.java).apply {
            putExtra(AbsTagEditorActivity.EXTRA_ID, songId)
        }
        activity.startActivity(intent)
    }

}