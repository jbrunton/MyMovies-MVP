package com.jbrunton.mymovies.networking.repositories

import androidx.collection.LruCache
import com.jbrunton.async.doOnSuccess
import com.jbrunton.mymovies.entities.models.Configuration
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.*
import com.jbrunton.mymovies.networking.resources.account.FavoriteRequest
import com.jbrunton.mymovies.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.mymovies.networking.resources.movies.MoviesCollection
import com.jbrunton.mymovies.networking.services.MovieService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.onEach

class HttpMoviesRepository(
        private val service: MovieService,
        private val preferences: ApplicationPreferences
): MoviesRepository {
    private val cache = LruCache<String, Movie>(1024)
    private var favoritesCache: List<Movie>? = null

    override suspend fun getMovie(movieId: String): DataStream<Movie>  {
        return buildDataStream(cache.get(movieId)) {
            coroutineScope {
                val movie = async { service.movie(movieId) }
                val config = async { config() }
                MovieDetailsResponse.toMovie(movie.await(), config.await()).apply {
                    cache.put(id, this)
                }
            }
        }
    }

    override suspend fun searchMovies(query: String): DataStream<List<Movie>> {
        return buildResponse({ service.search(query) })
    }

    override suspend fun nowPlaying(): DataStream<List<Movie>> {
        return buildResponse({ service.nowPlaying() })
    }

    override suspend fun popular(): DataStream<List<Movie>> {
        return buildResponse({ service.popular() })
    }

    override suspend fun discoverByGenre(genreId: String): DataStream<List<Movie>> {
        return buildResponse({ service.discoverByGenre(genreId) })
    }

    override suspend fun favorites(): DataStream<List<Movie>> {
        return buildResponse(
                { service.favorites(preferences.accountId, preferences.sessionId) },
                favoritesCache

        ).onEach {
            it.doOnSuccess {
                it.value.forEach { cache.put(it.id, it) }
                favoritesCache = it.value
            }
        }
    }

    override suspend fun favorite(movieId: String): DataStream<Unit> {
        return buildDataStream {
            service.favorite(
                    preferences.accountId,
                    preferences.sessionId,
                    FavoriteRequest(mediaId = movieId, favorite = true)
            ).apply {
                preferences.addFavorite(movieId)
            }
        }
    }

    override suspend fun unfavorite(movieId: String): DataStream<Unit> {
        return buildDataStream {
            service.favorite(
                    preferences.accountId,
                    preferences.sessionId,
                    FavoriteRequest(mediaId = movieId, favorite = false)
            ).apply {
                preferences.removeFavorite(movieId)
            }
        }
    }

    private suspend fun buildResponse(
            apiSource: suspend () -> MoviesCollection,
            cachedValue: List<Movie>? = null
    ): DataStream<List<Movie>> = coroutineScope {
        buildDataStream(cachedValue) {
            coroutineScope {
                val response = async { apiSource() }
                val config = async { config() }
                MoviesCollection.toCollection(response.await(), config.await())
            }
        }
    }

    private suspend fun config(): Configuration {
        return service.configuration().toModel()
    }
}
