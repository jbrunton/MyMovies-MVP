package com.jbrunton.mymovies.ui.moviedetails

import android.content.Context
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.SnackbarEvent
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetails

class MovieDetailsViewStateFactory(val context: Context) {
    fun viewState(result: AsyncResult<MovieDetails>): LoadingViewState<MovieDetailsViewState> {
        return LoadingViewState.build(MovieDetailsViewState.Empty).map(result) {
            MovieDetailsViewState.from(it.movie, it.favorite)
        }
    }

    val FavoriteAddedEvent = { action: () -> Unit -> SnackbarEvent(
            context.getString(R.string.added_to_favorites),
            context.getString(R.string.snackbar_undo),
            action)
    }

    val FavoriteRemovedEvent = { action: () -> Unit -> SnackbarEvent(
            context.getString(R.string.removed_from_favorites),
            context.getString(R.string.snackbar_undo),
            action)
    }

    val SignedOutEvent = SnackbarEvent(
            context.getString(R.string.sign_in_to_add_favorites),
            context.getString(R.string.snackbar_ok)
    )
}