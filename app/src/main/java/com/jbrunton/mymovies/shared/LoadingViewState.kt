package com.jbrunton.mymovies.shared

import androidx.annotation.DrawableRes
import com.jbrunton.entities.models.LoadingState

sealed class LoadingViewState<out T> {
    companion object {
//        fun <T>fromError(throwable: Throwable, cachedValue: T? = null): LoadingViewState<T> {
//            val error = DescriptiveError.from(throwable)
//            @DrawableRes val resId = if (error.isNetworkError)
//                R.drawable.ic_sentiment_dissatisfied_black_24dp
//            else
//                R.drawable.ic_sentiment_very_dissatisfied_black_24dp
//            return Failure(errorMessage = error.message,
//                    errorIcon = resId,
//                    showTryAgainButton = true,
//                    cachedValue = cachedValue)
//        }

        fun <S, T>from(state: LoadingState<S>, converter: (value: S) -> T): LoadingViewState<T> {
            return when (state) {
                is LoadingState.Success -> Success(converter(state.value))
                is LoadingState.Loading -> Loading(state.cachedValue?.let(converter))
                is LoadingState.Failure -> Failure(state.throwable, state.cachedValue?.let(converter))
            }
        }

        fun<T>failure(errorMessage: String,
                      @DrawableRes errorIcon: Int = 0,
                      allowRetry: Boolean = false
        ): LoadingViewState<T> {
            return Failure(LoadingViewStateError(errorMessage, errorIcon, allowRetry))
        }
    }

    data class Success<T>(val value: T): LoadingViewState<T>()
    data class Loading<T>(val cachedValue: T? = null) : LoadingViewState<T>()
    data class Failure<T>(val error: Throwable, val cachedValue: T? = null): LoadingViewState<T>()
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

class LoadingStateBuilder<S, T>(converter: (value: S) -> T) {
    var onSuccess: ((LoadingState.Success<S>) -> LoadingViewState<T>) = { LoadingViewState.Success(converter(it.value)) }
    var onLoading: ((LoadingState.Loading<S>) -> LoadingViewState<T>) = { LoadingViewState.Loading(it.cachedValue?.let(converter)) }
    var onFailure: ((LoadingState.Failure<S>) -> LoadingViewState<T>) = { LoadingViewState.Failure(it.throwable, it.cachedValue?.let(converter)) }

    fun onSuccess(onSuccess: (LoadingState.Success<S>) -> LoadingViewState<T>): Unit {
        this.onSuccess = onSuccess
    }

    fun onLoading(onLoading: (LoadingState.Loading<S>) -> LoadingViewState<T>): Unit {
        this.onLoading = onLoading
    }

    fun onFailure(onFailure: (LoadingState.Failure<S>) -> LoadingViewState<T>): Unit {
        this.onFailure = onFailure
    }

    fun build(state: LoadingState<S>): LoadingViewState<T> {
        return when (state) {
            is LoadingState.Success -> onSuccess(state)
            is LoadingState.Loading -> onLoading(state)
            is LoadingState.Failure -> onFailure(state)
        }
    }
}

fun <S, T>buildViewState(state: LoadingState<S>, converter: (value: S) -> T, init: LoadingStateBuilder<S, T>.() -> Unit): LoadingViewState<T> {
    val builder: LoadingStateBuilder<S, T> = LoadingStateBuilder(converter)
    builder.init()
    return builder.build(state)
}