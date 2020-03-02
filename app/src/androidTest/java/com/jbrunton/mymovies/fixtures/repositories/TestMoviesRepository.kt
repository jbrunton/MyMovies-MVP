package com.jbrunton.mymovies.fixtures.repositories

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import kotlinx.coroutines.flow.flowOf
import java.util.*
import kotlin.collections.HashMap

class TestMoviesRepository : MoviesRepository {
    override suspend fun favorites(): DataStream<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun favorite(movieId: String): DataStream<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun unfavorite(movieId: String): DataStream<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val movies = LinkedList<Movie>()
    private val stubbedSearches = HashMap<String, List<Movie>>()

    fun stubWith(movies: List<Movie>) {
        this.movies.clear()
        this.movies.addAll(movies)
    }

    fun stubSearch(query: String, results: List<Movie>) {
        stubbedSearches[query] = results
    }

    override suspend fun getMovie(movieId: String): DataStream<Movie> {
        return flowOf(AsyncResult.Success(movies.find { it.id == movieId }!!))
    }

    override suspend fun searchMovies(query: String): DataStream<List<Movie>> {
        return flowOf(AsyncResult.Success(stubbedSearches[query]!!))
    }

    override suspend fun nowPlaying(): DataStream<List<Movie>> {
        return flowOf(AsyncResult.Success(movies))
    }

    override suspend fun popular(): DataStream<List<Movie>> {
        return flowOf(AsyncResult.Success(movies))
    }

    override suspend fun discoverByGenre(genreId: String): DataStream<List<Movie>> {
        return flowOf(AsyncResult.Success(movies))
    }
}