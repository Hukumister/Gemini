package com.haroncode.gemini.sample.domain.model

sealed class Resource<out T> {

    object Loading : Resource<Nothing>()

    data class Data<T>(val value: T) : Resource<T>()

    data class Error(val throwable: Throwable) : Resource<Nothing>()
}