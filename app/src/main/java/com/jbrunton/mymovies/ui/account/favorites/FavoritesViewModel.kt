package com.jbrunton.mymovies.ui.account.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.bind
import com.jbrunton.entities.models.Account
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.search.BaseSearchViewModel
import io.reactivex.Observable

class FavoritesViewModel(
        val accountRepository: AccountRepository,
        val moviesRepository: MoviesRepository
) : BaseSearchViewModel() {
    override fun start() {
        loadFavorites()
    }

    fun retry() {
        loadFavorites()
    }

    private fun loadFavorites() {
        search {
            accountRepository
                    .account()
                    .flatMap(this::getFavoritesForAccount)
        }
    }

    private fun getFavoritesForAccount(result: AsyncResult<Account>): DataStream<List<Movie>> {
        return result.bind(
                onSuccess = { moviesRepository.favorites(it.value.id, accountRepository.session?.sessionId ?: "") },
                onLoading = { Observable.just(AsyncResult.loading(emptyList())) },
                onFailure = { Observable.just(AsyncResult.failure(it.error, emptyList())) }
        )
    }
}
