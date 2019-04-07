package com.jbrunton.mymovies.usecases.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.getOr
import com.jbrunton.async.map
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.entities.safelySubscribe
import com.jbrunton.mymovies.usecases.BaseUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.PublishSubject

sealed class DiscoverStateChange {
    data class DiscoverResultsAvailable(val discoverState: AsyncResult<DiscoverState>) : DiscoverStateChange()
    data class GenreSelected(val selectedGenre: Genre) : DiscoverStateChange()
    data class GenreResultsAvailable(val genreResults: AsyncResult<List<Movie>>) : DiscoverStateChange()
    object SelectedGenreCleared : DiscoverStateChange()
}

class DiscoverUseCase(
        val movies: MoviesRepository,
        val genres: GenresRepository
) : BaseUseCase() {
    val state = PublishSubject.create<AsyncResult<DiscoverState>>()
    private val loadIntent = PublishSubject.create<Unit>()
    private val selectGenreIntent = PublishSubject.create<Genre>()
    private val clearSelectedGenreIntent = PublishSubject.create<Unit>()
    
    private val load = loadIntent.flatMap {
        browse().map(DiscoverStateChange::DiscoverResultsAvailable)
                .compose(schedulerContext.applySchedulers())
    }
    
    private val selectGenre = selectGenreIntent.flatMap {
        val selectedChange: DiscoverStateChange = DiscoverStateChange.GenreSelected(it)
        movies.discoverByGenre(it.id).map(this::buildGenreResults)
                .compose(schedulerContext.applySchedulers())
                .startWith(selectedChange)
    }
    
    private val clearSelectedGenre = clearSelectedGenreIntent.flatMap {
        Observable.just(DiscoverStateChange.SelectedGenreCleared)
    }

    override fun start(schedulerContext: SchedulerContext) {
        super.start(schedulerContext)

        val initialState: AsyncResult<DiscoverState> = AsyncResult.loading(null)
        Observable.merge(load, selectGenre, clearSelectedGenre)
                .scan(initialState, this::reduce)
                .distinctUntilChanged()
                .safelySubscribe(this, state::onNext)

        loadIntent.onNext(Unit)
    }

    fun retry() {
        loadIntent.onNext(Unit)
    }

    fun selectedGenre(genre: Genre) {
        selectGenreIntent.onNext(genre)
    }

    fun clearSelectedGenre() {
        clearSelectedGenreIntent.onNext(Unit)
    }

    private fun buildGenreResults(genreResults: AsyncResult<List<Movie>>): DiscoverStateChange {
        return DiscoverStateChange.GenreResultsAvailable(genreResults)
    }

    private fun reduce(previousState: AsyncResult<DiscoverState>, change: DiscoverStateChange): AsyncResult<DiscoverState> {
        return when (change) {
            is DiscoverStateChange.DiscoverResultsAvailable -> change.discoverState
            is DiscoverStateChange.GenreSelected -> previousState.map {
                it.copy(selectedGenre = change.selectedGenre)
            }
            is DiscoverStateChange.GenreResultsAvailable -> previousState.map {
                it.copy(genreResults = change.genreResults.getOr(emptyList()))
            }
            is DiscoverStateChange.SelectedGenreCleared -> previousState.map {
                it.copy(genreResults = emptyList(), selectedGenre = null)
            }
        }
    }

    private fun browse(): DataStream<DiscoverState> {
        return Observables.zip(
                movies.nowPlaying(),
                movies.popular(),
                genres.genres()
        ) { nowPlaying, popular, genres ->
            AsyncResult.zip(nowPlaying, popular, genres, ::DiscoverState)
                    .handleNetworkErrors()
        }
    }
}