package com.jbrunton.mymovies.ui.discover

import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import io.reactivex.rxkotlin.Observables

class DiscoverViewModel internal constructor(
        private val moviesRepository: MoviesRepository,
        private val genresRepository: GenresRepository
) : BaseLoadingViewModel<DiscoverViewState>() {
    override fun start() {
        load()
    }

    override fun retry() {
        load()
    }

    private fun load() {
        val discoverStream = Observables.zip(
                moviesRepository.nowPlaying(),
                moviesRepository.popular(),
                genresRepository.genres()
        ) {
            nowPlaying, popular, genres -> DiscoverViewStateFactory.from(nowPlaying, popular, genres)
        }
        subscribe(discoverStream) {
            viewState.postValue(it)
        }
    }
}
