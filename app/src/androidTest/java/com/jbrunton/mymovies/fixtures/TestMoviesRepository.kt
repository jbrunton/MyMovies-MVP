package com.jbrunton.mymovies.fixtures

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.Observable
import java.util.*
import kotlin.collections.HashMap

class TestMoviesRepository : MoviesRepository {
    override fun favorites(): DataStream<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun favorite(movieId: String): Observable<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unfavorite(movieId: String): Observable<Any> {
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

    override fun getMovie(movieId: String): DataStream<Movie> {
        return Observable.just(AsyncResult.Success(movies.find { it.id == movieId }!!))
    }

    override fun searchMovies(query: String): DataStream<List<Movie>> {
        return Observable.just(AsyncResult.Success(stubbedSearches[query]!!));
    }

    override fun nowPlaying(): DataStream<List<Movie>> {
        return Observable.just(AsyncResult.Success(movies))
    }

    override fun discoverByGenre(genreId: String): DataStream<List<Movie>> {
        return Observable.just(AsyncResult.Success(movies))
    }
}