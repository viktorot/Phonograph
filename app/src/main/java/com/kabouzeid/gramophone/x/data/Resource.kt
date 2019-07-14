package com.kabouzeid.gramophone.x.data

sealed class Resource<T>

class Loading<T> : Resource<T>()

data class Done<T>(val data: T) : Resource<T>()

data class Error<T>(val error: Throwable? = null) : Resource<T>()