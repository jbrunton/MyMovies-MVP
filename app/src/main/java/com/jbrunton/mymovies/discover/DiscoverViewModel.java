package com.jbrunton.mymovies.discover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.jbrunton.entities.Movie;
import com.jbrunton.entities.MoviesRepository;
import com.jbrunton.mymovies.search.SearchViewState;
import com.jbrunton.mymovies.search.SearchViewStateFactory;
import com.jbrunton.mymovies.shared.BaseViewModel;
import com.jbrunton.mymovies.shared.LoadingViewState;

import java.util.Collections;
import java.util.List;

public class DiscoverViewModel extends BaseViewModel {
    private final MoviesRepository repository;
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final SearchViewStateFactory viewStateFactory = new SearchViewStateFactory();

    DiscoverViewModel(MoviesRepository repository) {
        this.repository = repository;
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    @Override public void start() {
        viewState.setValue(
                new SearchViewState(
                        LoadingViewState.LOADING_STATE,
                        Collections.emptyList()));
        repository.nowPlaying()
                .compose(applySchedulers())
                .subscribe(this::setMoviesResponse, this::setErrorResponse);
    }

    private void setMoviesResponse(List<Movie> movies) {
        viewState.setValue(viewStateFactory.fromList(movies));
    }

    private void setErrorResponse(Throwable error) {
        viewState.setValue(viewStateFactory.fromError(error));
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final MoviesRepository repository;

        public Factory(MoviesRepository repository) {
            this.repository = repository;
        }

        @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DiscoverViewModel(repository);
        }
    }
}
