package com.haroncode.gemini.common

inline fun <F, S, R> combineNullable(
    first: F?,
    second: S?,
    crossinline combiner: (F, S) -> R
): R? = first?.let { firstNotNull ->
    second?.let { secondNotNull -> combiner(firstNotNull, secondNotNull) }
}
