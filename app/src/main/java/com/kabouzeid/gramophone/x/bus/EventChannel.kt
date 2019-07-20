package com.kabouzeid.gramophone.x.bus

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class EventChannel<T> {

    private val channel = Channel<T>(capacity = Channel.UNLIMITED)

    fun consume(scope: CoroutineScope = GlobalScope, block: (T) -> Unit) = scope.launch {
        for(event in channel) {
            block(event)
        }
    }

    fun send(item: T, scope: CoroutineScope = GlobalScope) = scope.launch {
        channel.send(item)
    }

    fun close() {
        channel.close()
    }
}