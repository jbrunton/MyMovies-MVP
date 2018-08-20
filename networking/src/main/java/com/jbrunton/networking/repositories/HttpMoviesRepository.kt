package com.jbrunton.networking.repositories

import com.jbrunton.entities.Configuration
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.networking.resources.movies.MoviesCollection
import com.jbrunton.networking.services.MovieService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables

class HttpMoviesRepository(private val service: MovieService) : MoviesRepository {

    override fun getMovieLegacy(movieId: String): Observable<Movie> {
        return Observables.zip(service.movieRx(movieId), legacyConfig()) {
            response, config -> MovieDetailsResponse.toMovie(response, config)
        }
    }

    override suspend fun getMovie(movieId: String): Movie {
        val movie = service.movie(movieId);
        val config = service.configuration();
        return MovieDetailsResponse.toMovie(
                    movie.await(),
                    config.await().toModel())
    }

    override fun searchMovies(query: String): Observable<List<Movie>> {
        return Observables.zip(service.search(query), legacyConfig()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    override fun nowPlaying(): Observable<List<Movie>> {
        return Observables.zip(service.nowPlaying(), legacyConfig()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    override fun discoverByGenre(genreId: String): Observable<List<Movie>> {
        return Observables.zip(service.discoverByGenre(genreId), legacyConfig()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    private fun legacyConfig(): Observable<Configuration> {
        return service.configurationRx().map { it.toModel() }
    }
//
//    private fun config(): Deferred<Configuration> {
//        return deferredService.configurationRx().await().toModel()
//    }
}
