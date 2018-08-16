package com.jbrunton.networking.repositories

import com.jbrunton.entities.Configuration
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.networking.resources.movies.MoviesCollection
import com.jbrunton.networking.services.LegacyMovieService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables

class HttpMoviesRepository(private val service: LegacyMovieService) : MoviesRepository {

    override fun getMovie(movieId: String): Observable<Movie> {
        return Observables.zip(service.movie(movieId), config()) {
            response, config -> MovieDetailsResponse.toMovie(response, config)
        }
    }

    override fun searchMovies(query: String): Observable<List<Movie>> {
        return Observables.zip(service.search(query), config()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    override fun nowPlaying(): Observable<List<Movie>> {
        return Observables.zip(service.nowPlaying(), config()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    override fun discoverByGenre(genreId: String): Observable<List<Movie>> {
        return Observables.zip(service.discoverByGenre(genreId), config()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    private fun config(): Observable<Configuration> {
        return service.configuration().map { it.toModel() }
    }
}
