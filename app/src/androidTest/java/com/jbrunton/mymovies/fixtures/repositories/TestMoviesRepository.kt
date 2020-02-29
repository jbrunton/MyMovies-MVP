package com.jbrunton.mymovies.fixtures.repositories

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.FlowDataStream
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import io.reactivex.Observable
import kotlinx.coroutines.flow.flowOf
import java.util.*
import kotlin.collections.HashMap

class TestMoviesRepository : MoviesRepository {
    override suspend fun favorites(): FlowDataStream<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun favorite(movieId: String): FlowDataStream<Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun unfavorite(movieId: String): FlowDataStream<Unit> {
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

    override suspend fun getMovie(movieId: String): FlowDataStream<Movie> {
        return flowOf(AsyncResult.Success(movies.find { it.id == movieId }!!))
    }

    override fun searchMovies(query: String): DataStream<List<Movie>> {
        return Observable.just(AsyncResult.Success(stubbedSearches[query]!!));
    }

    override suspend fun nowPlaying(): FlowDataStream<List<Movie>> {
        return flowOf(AsyncResult.Success(movies))
    }

    override suspend fun popular(): FlowDataStream<List<Movie>> {
        return flowOf(AsyncResult.Success(movies))
    }

    override suspend fun discoverByGenre(genreId: String): FlowDataStream<List<Movie>> {
        return flowOf(AsyncResult.Success(movies))
    }
}