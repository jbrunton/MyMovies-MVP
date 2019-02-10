package com.jbrunton.mymovies.ui.search

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchUseCase(val repository: MoviesRepository) {
    fun search(queries: Observable<String>): Observable<LoadingViewState<SearchViewState>> {
        return queries
                .switchMap {
                    repository.searchMovies(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
                .map { SearchViewStateFactory.from(it) }
    }
}