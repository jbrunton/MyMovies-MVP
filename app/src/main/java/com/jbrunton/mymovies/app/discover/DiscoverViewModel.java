package com.jbrunton.mymovies.app.discover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.app.converters.MovieResultsConverter;
import com.jbrunton.mymovies.app.search.SearchViewState;
import com.jbrunton.mymovies.app.shared.BaseViewModel;
import com.jbrunton.mymovies.app.shared.LoadingViewState;
import com.jbrunton.mymovies.models.Movie;

import java.util.Collections;
import java.util.List;

public class DiscoverViewModel extends BaseViewModel {
    private final MoviesRepository repository;
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MovieResultsConverter converter = new MovieResultsConverter();

    DiscoverViewModel() {
        repository = new MoviesRepository(ServiceFactory.instance());
        viewState.setValue(SearchViewState.builder()
                .setLoadingViewState(LoadingViewState.LOADING_STATE)
                .setMovies(Collections.emptyList())
                .build());
        repository.nowPlaying()
                .compose(applySchedulers())
                .subscribe(this::setMoviesResponse, this::setErrorResponse);
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    private void setMoviesResponse(List<Movie> movies) {
        viewState.setValue(converter.toSearchViewState(movies));
    }

    private void setErrorResponse(Throwable error) {
        viewState.setValue(converter.toSearchViewState(error));
    }
}
