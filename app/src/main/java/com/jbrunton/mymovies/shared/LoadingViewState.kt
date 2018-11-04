package com.jbrunton.mymovies.shared

import androidx.annotation.DrawableRes
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.mymovies.R
import com.jbrunton.networking.DescriptiveError

sealed class LoadingViewState<out T> {
    companion object {
        fun <T>fromError(throwable: Throwable, cachedValue: T? = null): LoadingViewState<T> {
            val error = DescriptiveError.from(throwable)
            @DrawableRes val resId = if (error.isNetworkError)
                R.drawable.ic_sentiment_dissatisfied_black_24dp
            else
                R.drawable.ic_sentiment_very_dissatisfied_black_24dp
            return Failure(errorMessage = error.message,
                    errorIcon = resId,
                    showTryAgainButton = true,
                    cachedValue = cachedValue)
        }

        fun <S, T>from(state: LoadingState<S>, converter: (value: S) -> T): LoadingViewState<T> {
            return when (state) {
                is LoadingState.Success -> Success(value = converter(state.value))
                is LoadingState.Loading -> Loading(cachedValue = state.cachedValue?.let(converter))
                is LoadingState.Failure -> fromError(state.throwable, state.cachedValue?.let(converter))
            }
        }
    }

    data class Success<T>(val value: T): LoadingViewState<T>()

    data class Failure<T>(
            val errorMessage: String,
            @DrawableRes val errorIcon: Int,
            val showTryAgainButton: Boolean = false,
            val cachedValue: T? = null) : LoadingViewState<T>()

    data class Loading<T>(val cachedValue: T? = null) : LoadingViewState<T>()
}

fun <S, T>LoadingState<S>.toViewState(converter: (value: S) -> T): LoadingViewState<T> {
    return LoadingViewState.from(this, converter)
}

fun <T>LoadingViewState<T>.map(onSuccess: (value: T) -> LoadingViewState<T>): LoadingViewState<T> {
    return when (this) {
        is LoadingViewState.Success -> onSuccess(this.value)
        is LoadingViewState.Loading -> this
        is LoadingViewState.Failure -> this
    }
}

fun <T>LoadingState<T>.toViewState(): LoadingViewState<T> {
    return LoadingViewState.from(this, { it })
}