package com.jbrunton.networking.repositories

import com.jbrunton.entities.Configuration
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.networking.DescriptiveError
import com.jbrunton.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.networking.resources.movies.MoviesCollection
import com.jbrunton.networking.services.DeferredMovieService
import com.jbrunton.networking.services.RxMovieService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import retrofit2.HttpException
import java.io.IOException

class HttpMoviesRepository(private val rxService: RxMovieService, private val deferredService: DeferredMovieService) : MoviesRepository {

    override fun getMovieLegacy(movieId: String): Observable<Movie> {
        return Observables.zip(rxService.movie(movieId), legacyConfig()) {
            response, config -> MovieDetailsResponse.toMovie(response, config)
        }
    }

    override suspend fun getMovie(movieId: String): Movie {
        try {
            val movie = deferredService.movie(movieId);
            val config = deferredService.configuration();
            return MovieDetailsResponse.toMovie(
                    movie.await(),
                    config.await().toModel())
        } catch (e: HttpException) {
            throw DescriptiveError.from(e)
        } catch (e: IOException) {
            throw DescriptiveError.from(e)
        }
    }

    override fun searchMovies(query: String): Observable<List<Movie>> {
        return Observables.zip(rxService.search(query), legacyConfig()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    override fun nowPlaying(): Observable<List<Movie>> {
        return Observables.zip(rxService.nowPlaying(), legacyConfig()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    override fun discoverByGenre(genreId: String): Observable<List<Movie>> {
        return Observables.zip(rxService.discoverByGenre(genreId), legacyConfig()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    private fun legacyConfig(): Observable<Configuration> {
        return rxService.configuration().map { it.toModel() }
    }
//
//    private fun config(): Deferred<Configuration> {
//        return deferredService.configuration().await().toModel()
//    }
}
