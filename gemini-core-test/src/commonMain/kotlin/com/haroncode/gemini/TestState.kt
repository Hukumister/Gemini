package com.haroncode.gemini

const val INITIAL_COUNTER = 10
const val INITIAL_LOADING = false

data class TestState(
    val id: Long = 1L,
    val counter: Int = INITIAL_COUNTER,
    val loading: Boolean = INITIAL_LOADING
)
