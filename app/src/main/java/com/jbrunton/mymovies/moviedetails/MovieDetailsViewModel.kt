package com.jbrunton.mymovies.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.shared.BaseViewModel
import com.jbrunton.mymovies.shared.CoroutineDispatchers
import com.jbrunton.networking.DescriptiveError
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailsViewModel(
        private val movieId: String,
        private val repository: MoviesRepository,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel() {
    private val viewState = MutableLiveData<MovieDetailsViewState>()
    private val viewStateFactory = MovieDetailsViewStateFactory()

    fun viewState(): LiveData<MovieDetailsViewState> {
        return viewState
    }

    override fun start() {
        loadDetails()
    }

    fun retry() {
        loadDetails()
    }

    private fun loadDetails() {
        viewState.setValue(viewStateFactory.loadingState())
        launch(dispatchers.Main) {
            try {
                val movie = withContext(dispatchers.IO) {
                    repository.getMovie(movieId)
                }
                setMovieResponse(movie)
            } catch (e: DescriptiveError) {
                setErrorResponse(e)
            }
        }
    }

    private fun setMovieResponse(movie: Movie) {
        viewState.value = viewStateFactory.fromMovie(movie)
    }

    private fun setErrorResponse(throwable: Throwable) {
        viewState.value = viewStateFactory.fromError(throwable)
    }
}
