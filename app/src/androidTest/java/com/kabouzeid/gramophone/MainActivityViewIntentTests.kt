package com.kabouzeid.gramophone

import android.content.Intent
import android.net.Uri
import androidx.test.core.app.launchActivity
import androidx.test.rule.ActivityTestRule
import com.kabouzeid.gramophone.ui.activities.MainActivity
import org.junit.Rule
import org.junit.Test

class MainActivityViewIntentTests {

    @Rule @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun test_intent() {
        val intent = Intent().apply {
            type = Intent.ACTION_VIEW
            data = Uri.parse("file:///storage/emulated/0/Download/file.mp3")
        }
        activityRule.launchActivity(intent)
    }
}