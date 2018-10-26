package com.jbrunton.mymovies.discover

import androidx.annotation.DrawableRes
import com.jbrunton.entities.Genre
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.shared.LegacyLoadingViewState
import com.jbrunton.networking.DescriptiveError

class GenresViewStateFactory {
    fun fromList(genres: List<Genre>): GenresViewState {
        return if (genres.isEmpty()) {
            emptyViewState(LegacyLoadingViewState.errorBuilder()
                    .setErrorMessage("Unexpected Error")
                    .setErrorIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
                    .setShowTryAgainButton(true)
                    .build())
        } else {
            GenresViewState(
                    LegacyLoadingViewState.builder()
                            .setCurrentState(LegacyLoadingViewState.State.OK)
                            .build(),
                    genres)
        }
    }

    fun fromError(throwable: Throwable): GenresViewState {
        val error = DescriptiveError.from(throwable)
        @DrawableRes val resId = if (error.isNetworkError) R.drawable.ic_sentiment_dissatisfied_black_24dp else R.drawable.ic_sentiment_very_dissatisfied_black_24dp
        return emptyViewState(LegacyLoadingViewState.errorBuilder()
                .setErrorMessage(error.message)
                .setErrorIcon(resId)
                .setShowTryAgainButton(true)
                .build())
    }

    fun emptyViewState(loadingViewState: LegacyLoadingViewState): GenresViewState {
        return GenresViewState(loadingViewState, emptyList())
    }
}
