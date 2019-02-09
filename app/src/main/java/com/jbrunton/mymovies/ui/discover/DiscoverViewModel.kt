package com.jbrunton.mymovies.ui.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onSuccess
import com.jbrunton.async.zipWith
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.search.SearchViewStateFactory
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors
import com.jbrunton.mymovies.ui.shared.toLoadingViewState
import io.reactivex.rxkotlin.Observables

class DiscoverViewModel internal constructor(private val repository: MoviesRepository) : BaseLoadingViewModel<DiscoverViewState>() {
    override fun start() {
        load()
    }

    fun retry() {
        load()
    }

    private fun load() {
        val nowPlayingStream = repository.nowPlaying().map(this::handleErrors)
        val popularStream = repository.popular().map(this::handleErrors)
        val discoverStream = Observables.zip(nowPlayingStream, popularStream) {
            nowPlaying, popular -> nowPlaying.zipWith(popular, ::DiscoverViewState)
        }
        discoverStream.compose(applySchedulers())
                .subscribe(this::handleResponse)
    }

    private fun handleResponse(result: AsyncResult<DiscoverViewState>) {
        val viewState = result.toLoadingViewState(DiscoverViewState(emptyList(), emptyList()))
        this.viewState.postValue(viewState)
    }

    private fun handleErrors(result: AsyncResult<List<Movie>>): AsyncResult<List<MovieSearchResultViewState>> {
        return result.map(SearchViewStateFactory.Companion::toViewState)
                .handleNetworkErrors()
                .onSuccess {
                    SearchViewStateFactory.errorIfEmpty(it.value)
                }
    }
}
