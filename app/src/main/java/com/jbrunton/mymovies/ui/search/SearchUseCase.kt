package com.jbrunton.mymovies.ui.search

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchUseCase(val repository: MoviesRepository) {
    fun start(queries: Observable<String>): Observable<LoadingViewState<SearchViewState>> {
        return queries
                .switchMap(this::search)
                .startWith(SearchViewStateFactory.EmptyState)
    }

    private fun search(query: String): Observable<LoadingViewState<SearchViewState>> {
        if (query.isEmpty()) {
            return Observable.just(SearchViewStateFactory.EmptyState)
        }

        return repository.searchMovies(query)
                .map { SearchViewStateFactory.from(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}