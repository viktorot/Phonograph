package com.kabouzeid.gramophone.x.theming

fun getMaxGridItemCount(landscape: Boolean): Int {
    return when (landscape) {
        true -> 6
        false -> 4
    }
}

fun getMaxListItemCount(landscape: Boolean): Int {
    return when (landscape) {
        true -> 2
        false -> 1
    }
}

fun showGrid(size: Int): Boolean {
    return size > 1
}