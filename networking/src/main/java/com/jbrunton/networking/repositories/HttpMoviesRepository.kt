package com.jbrunton.networking.repositories

import com.jbrunton.entities.Configuration
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.networking.DescriptiveError
import com.jbrunton.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.networking.resources.movies.MoviesCollection
import com.jbrunton.networking.services.LegacyMovieService
import com.jbrunton.networking.services.MovieService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import retrofit2.HttpException
import java.io.IOException

class HttpMoviesRepository(private val legacyService: LegacyMovieService, private val service: MovieService) : MoviesRepository {

    override fun getMovieLegacy(movieId: String): Observable<Movie> {
        return Observables.zip(legacyService.movie(movieId), legacyConfig()) {
            response, config -> MovieDetailsResponse.toMovie(response, config)
        }
    }

    override suspend fun getMovie(movieId: String): Movie {
        try {
            return MovieDetailsResponse.toMovie(
                    service.movie(movieId).await(),
                    service.configuration().await().toModel())
        } catch (e: HttpException) {
            throw DescriptiveError.from(e)
        } catch (e: IOException) {
            throw DescriptiveError.from(e)
        }
    }

    override fun searchMovies(query: String): Observable<List<Movie>> {
        return Observables.zip(legacyService.search(query), legacyConfig()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    override fun nowPlaying(): Observable<List<Movie>> {
        return Observables.zip(legacyService.nowPlaying(), legacyConfig()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    override fun discoverByGenre(genreId: String): Observable<List<Movie>> {
        return Observables.zip(legacyService.discoverByGenre(genreId), legacyConfig()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }
    }

    private fun legacyConfig(): Observable<Configuration> {
        return legacyService.configuration().map { it.toModel() }
    }
//
//    private fun config(): Deferred<Configuration> {
//        return service.configuration().await().toModel()
//    }
}
