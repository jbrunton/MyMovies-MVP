package com.jbrunton.mymovies.fixtures

import com.jbrunton.entities.models.DataStream
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.Observable
import java.util.*
import kotlin.collections.HashMap

class TestMoviesRepository : MoviesRepository {
    private val movies = LinkedList<Movie>()
    private val stubbedSearches = HashMap<String, List<Movie>>()

    fun stubWith(movies: List<Movie>) {
        this.movies.clear()
        this.movies.addAll(movies)
    }

    fun stubSearch(query: String, results: List<Movie>) {
        stubbedSearches[query] = results
    }

    override fun getMovie(movieId: String): DataStream<Movie> {
        return Observable.just(LoadingState.Success(movies.find { it.id == movieId }!!))
    }

    override fun searchMovies(query: String): DataStream<List<Movie>> {
        return Observable.just(LoadingState.Success(stubbedSearches[query]!!));
    }

    override fun nowPlaying(): DataStream<List<Movie>> {
        return Observable.just(LoadingState.Success(movies))
    }

    override fun discoverByGenre(genreId: String): DataStream<List<Movie>> {
        return Observable.just(LoadingState.Success(movies))
    }
}