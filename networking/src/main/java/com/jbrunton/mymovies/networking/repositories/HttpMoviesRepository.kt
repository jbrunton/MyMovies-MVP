package com.jbrunton.mymovies.networking.repositories

import androidx.collection.LruCache
import com.jbrunton.async.doOnSuccess
import com.jbrunton.mymovies.entities.models.Configuration
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.ApplicationPreferences
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.entities.repositories.toDataStream
import com.jbrunton.mymovies.networking.resources.account.FavoriteRequest
import com.jbrunton.mymovies.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.mymovies.networking.resources.movies.MoviesCollection
import com.jbrunton.mymovies.networking.services.MovieService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables

class HttpMoviesRepository(
        private val service: MovieService,
        private val preferences: ApplicationPreferences
): MoviesRepository {
    private val cache = LruCache<String, Movie>(1024)
    private var favoritesCache: List<Movie>? = null

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

    override fun popular(): DataStream<List<Movie>> {
        return buildResponse(service.popular())
    }

    override fun discoverByGenre(genreId: String): DataStream<List<Movie>> {
        return buildResponse(service.discoverByGenre(genreId))
    }

    override fun favorites(): DataStream<List<Movie>> {
        return buildResponse(
                service.favorites(preferences.accountId, preferences.sessionId),
                favoritesCache

        ).doOnNext {
            it.doOnSuccess {
                it.value.forEach { cache.put(it.id, it) }
                favoritesCache = it.value
            }
        }
    }

    override fun favorite(movieId: String): DataStream<Unit> {
        return service.favorite(
                preferences.accountId,
                preferences.sessionId,
                FavoriteRequest(mediaId = movieId, favorite = true)
        ).map { Unit }.doOnNext {
            preferences.addFavorite(movieId)
        }.toDataStream()
    }

    override fun unfavorite(movieId: String): DataStream<Unit> {
        return service.favorite(
                preferences.accountId,
                preferences.sessionId,
                FavoriteRequest(mediaId = movieId, favorite = false)
        ).map { Unit }.doOnNext {
            preferences.removeFavorite(movieId)
        }.toDataStream()
    }

    private fun buildResponse(
            apiSource: Observable<MoviesCollection>,
            cachedValue: List<Movie>? = null
    ): DataStream<List<Movie>> {
        return Observables.zip(apiSource, config()) {
            response, config -> MoviesCollection.toCollection(response, config)
        }.toDataStream(cachedValue)
    }

    private fun config(): Observable<Configuration> {
        return service.configuration().map { it.toModel() }
    }
}
