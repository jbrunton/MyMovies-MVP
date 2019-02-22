package com.jbrunton.mymovies.ui.moviedetails

import android.content.Context
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.SnackbarEvent
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsSnackbar
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsState

class MovieDetailsViewStateFactory(val context: Context) {
    fun viewState(result: AsyncResult<MovieDetailsState>): LoadingViewState<MovieDetailsViewState> {
        return LoadingViewState.build(MovieDetailsViewState.Empty).map(result) {
            MovieDetailsViewState.from(it.movie, it.favorite)
        }
    }

    fun snackbar(
            state: MovieDetailsSnackbar,
            undoAddFavorite: () -> Unit,
            underRemoveFavorite: () -> Unit
    ): SnackbarEvent {
        return when (state) {
            is MovieDetailsSnackbar.FavoriteAdded -> FavoriteAddedEvent(undoAddFavorite)
            is MovieDetailsSnackbar.FavoriteRemoved -> FavoriteRemovedEvent(underRemoveFavorite)
            is MovieDetailsSnackbar.SignedOut -> SignedOutEvent
        }
    }

    private val FavoriteAddedEvent = { action: () -> Unit -> SnackbarEvent(
            context.getString(R.string.added_to_favorites),
            context.getString(R.string.snackbar_undo),
            action)
    }

    private val FavoriteRemovedEvent = { action: () -> Unit -> SnackbarEvent(
            context.getString(R.string.removed_from_favorites),
            context.getString(R.string.snackbar_undo),
            action)
    }

    private val SignedOutEvent = SnackbarEvent(
            context.getString(R.string.sign_in_to_add_favorites),
            context.getString(R.string.snackbar_ok)
    )
}