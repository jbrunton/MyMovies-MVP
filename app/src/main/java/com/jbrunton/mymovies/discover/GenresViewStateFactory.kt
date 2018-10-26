package com.jbrunton.mymovies.discover

import com.jbrunton.entities.Genre
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.shared.Failure
import com.jbrunton.mymovies.shared.Success

class GenresViewStateFactory {
    fun fromList(genres: List<Genre>): GenresViewState {
        return if (genres.isEmpty()) {
            Failure(errorMessage = "Unexpected Error",
                    errorIcon = R.drawable.ic_sentiment_very_dissatisfied_black_24dp,
                    showTryAgainButton = true)
        } else {
            Success(genres)
        }
    }
}
