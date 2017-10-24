package com.jbrunton.mymovies.discover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.jbrunton.mymovies.BaseViewModel;
import com.jbrunton.mymovies.LoadingViewState;
import com.jbrunton.mymovies.Movie;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.converters.MovieResultsConverter;
import com.jbrunton.mymovies.search.SearchViewState;

import java.util.Collections;
import java.util.List;

public class DiscoverViewModel extends BaseViewModel {
    private final MoviesRepository repository;
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MovieResultsConverter converter = new MovieResultsConverter();

    DiscoverViewModel() {
        repository = new MoviesRepository(ServiceFactory.instance());
        viewState.setValue(SearchViewState.builder()
                .setCurrentState(LoadingViewState.State.LOADING)
                .setMovies(Collections.emptyList())
                .build());
        repository.nowPlaying()
                .compose(applySchedulers())
                .subscribe(this::setResponse);
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    private void setResponse(MaybeError<List<Movie>> response) {
        response.ifValue(this::setMoviesResponse).elseIfError(this::setErrorResponse);
    }

    private void setMoviesResponse(List<Movie> movies) {
        viewState.setValue(converter.toSearchViewState(movies));
    }

    private void setErrorResponse(DescriptiveError error) {
        viewState.setValue(converter.toSearchViewState(error));
    }
}
