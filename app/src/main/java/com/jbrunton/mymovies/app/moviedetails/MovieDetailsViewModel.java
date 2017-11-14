package com.jbrunton.mymovies.app.moviedetails;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.app.shared.BaseViewModel;
import com.jbrunton.mymovies.models.Movie;

public class MovieDetailsViewModel extends BaseViewModel {
    private final MutableLiveData<MovieDetailsViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final String movieId;
    private final MovieDetailsViewStateFactory viewStateFactory = new MovieDetailsViewStateFactory();

    public MovieDetailsViewModel(String movieId, MoviesRepository repository) {
        this.movieId = movieId;
        this.repository = repository;
        loadDetails();
    }

    public LiveData<MovieDetailsViewState> viewState() {
        return viewState;
    }

    public void loadDetails() {
        viewState.setValue(viewStateFactory.loadingState());
        repository.getMovie(movieId)
                .compose(applySchedulers())
                .subscribe(this::setMovieResponse, this::setErrorResponse);
    }

    private void setMovieResponse(Movie movie) {
        viewState.setValue(viewStateFactory.fromMovie(movie));
    }

    private void setErrorResponse(Throwable throwable) {
        viewState.setValue(viewStateFactory.fromError(throwable));
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final String movieId;

        public Factory(String movieId) {
            this.movieId = movieId;
        }

        @Override public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MovieDetailsViewModel(movieId, new MoviesRepository(ServiceFactory.instance()));
        }
    }
}
