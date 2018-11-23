package com.jbrunton.fixtures

import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import io.reactivex.Observable
import kotlinx.coroutines.delay
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit

class FakeMovieRepository(private val movies: List<Movie>) : MoviesRepository {

    fun errorOnGet(movieId: String, throwable: Throwable) {

    }

    override fun getMovie(movieId: String): Observable<Movie> {
        val movie = movies.find { it.id == movieId }
                ?: throw IllegalStateException("Could not find movie with id = $movieId")
        return Observable.just(movie).delay(1, TimeUnit.SECONDS)
    }

    override fun searchMovies(query: String): Observable<List<Movie>> {
        val movies = movies.filter { it.title.contains(query) }
        return Observable.just(movies).delay(1, TimeUnit.SECONDS)
    }

    override fun nowPlaying(): Observable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun discoverByGenre(genreId: String): Observable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}