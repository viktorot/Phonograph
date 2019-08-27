package com.kabouzeid.gramophone.x.playback

import android.content.Context
import android.content.Intent

interface IPlaybackHandler {
    fun handle(intent: Intent, context: Context): Boolean
}