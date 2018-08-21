package com.jbrunton.networking.resources

abstract class PagedCollection<T> {
    abstract val page: Int
    abstract val total_results: Int
    abstract val total_pages: Int
    abstract val results: List<T>

    protected fun <S> toCollection(converter: (T) -> S): List<S> {
        return results.map { converter.invoke(it) }
    }
}
