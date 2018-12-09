package com.jbrunton.networking.repositories

import androidx.collection.LruCache
import com.jbrunton.async.doOnSuccess
import com.jbrunton.entities.models.Configuration
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.entities.repositories.toDataStream
import com.jbrunton.networking.resources.account.FavoriteRequest
import com.jbrunton.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.networking.resources.movies.MoviesCollection
import com.jbrunton.networking.services.MovieService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables

class HttpMoviesRepository(
        private val service: MovieService,
        private val preferences: ApplicationPreferences
): MoviesRepository {
    private val cache = LruCache<String, Movie>(1024)

    override fun getMovie(movieId: String): DataStream<Movie> {
        return Observables.zip(service.movie(movieId), config()) {
            response, config -> MovieDetailsResponse.toMovie(response, config)
        }.doOnNext {
            cache.put(it.id, it)
        }.toDataStream(cache.get(movieId))
    }

    override fun searchMovies(query: String): DataStream<List<Movie>> {
        return buildResponse(service.search(query))
    }

    override fun nowPlaying(): DataStream<List<Movie>> {
        return buildResponse(service.nowPlaying())
    }

    override fun discoverByGenre(genreId: String): DataStream<List<Movie>> {
        return buildResponse(service.discoverByGenre(genreId))
    }

    override fun favorites(): DataStream<List<Movie>> {
        return buildResponse(service.favorites(preferences.accountId!!, preferences.sessionId!!))
                .doOnNext {
                    it.doOnSuccess {
                        preferences.favorites = it.value.map { it.id }.toSet()
                    }
                }
    }

    override fun favorite(movieId: String): Observable<Any> {
        return service.favorite(
                preferences.accountId!!,
                preferences.sessionId!!,
                FavoriteRequest(mediaId = movieId, favorite = true)
        ).doOnNext {
            val favorites = (preferences.favorites ?: emptySet()).toMutableSet()
            favorites.add(movieId)
            preferences.favorites = favorites
        }
    }

    override fun unfavorite(movieId: String): Observable<Any> {
        return service.favorite(
                preferences.accountId!!,
                preferences.sessionId!!,
                FavoriteRequest(mediaId = movieId, favorite = false)
        ).doOnNext {
            val favorites = (preferences.favorites ?: emptySet()).toMutableSet()
            favorites.remove(movieId)
            preferences.favorites = favorites
        }
    }

    private fun buildResponse(apiSource: Observable<MoviesCollection>): DataStream<List<Movie>> {
        return Observables.zip(apiSource, config()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }.toDataStream()
    }

    private fun config(): Observable<Configuration> {
        return service.configuration().map { it.toModel() }
    }
}
