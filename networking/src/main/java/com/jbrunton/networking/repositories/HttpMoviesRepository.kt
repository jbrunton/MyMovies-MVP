package com.jbrunton.networking.repositories

import com.jbrunton.entities.models.Configuration
import com.jbrunton.entities.models.DataStream
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.networking.resources.movies.MoviesCollection
import com.jbrunton.networking.services.MovieService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables

class HttpMoviesRepository(private val service: MovieService): BaseRepository(), MoviesRepository {

    override fun getMovie(movieId: String): DataStream<Movie> {
        return buildResponse(Observables.zip(service.movie(movieId), config()) {
            response, config -> MovieDetailsResponse.toMovie(response, config)
        })
    }

    override fun searchMovies(query: String): DataStream<List<Movie>> {
        return buildResponse(Observables.zip(service.search(query), config()) {
            response, config -> MoviesCollection.toCollection(response, config)
        })
    }

    override fun nowPlaying(): DataStream<List<Movie>> {
        return buildResponse(Observables.zip(service.nowPlaying(), config()) {
            response, config -> MoviesCollection.toCollection(response, config)
        })
    }

    override fun discoverByGenre(genreId: String): DataStream<List<Movie>> {
        return buildResponse(Observables.zip(service.discoverByGenre(genreId), config()) {
            response, config -> MoviesCollection.toCollection(response, config)
        })
    }

    private fun config(): Observable<Configuration> {
        return service.configuration().map { it.toModel() }
    }
}
