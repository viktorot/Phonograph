package com.kabouzeid.gramophone

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.kabouzeid.gramophone.x.songs.SongsFragmentX
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongsFragmentTests {

    @Test
    fun test() {
        val scenario = launchFragmentInContainer<SongsFragmentX>()

//        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))

        onView(withText("[ERROR]")).check(matches(isDisplayed()))
    }
}
