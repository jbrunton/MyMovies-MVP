package com.jbrunton.mymovies.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.jbrunton.mymovies.BaseViewModel;
import com.jbrunton.mymovies.LoadingViewState;
import com.jbrunton.mymovies.Movie;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.converters.MovieResultsConverter;

import java.util.List;

public class SearchViewModel extends BaseViewModel {
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final MovieResultsConverter converter = new MovieResultsConverter();

    public SearchViewModel() {
        repository = new MoviesRepository(ServiceFactory.instance());
        viewState.setValue(SearchViewState.errorBuilder()
                .setErrorMessage("Search")
                .setErrorIcon(R.drawable.ic_search_black_24dp)
                .build());
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    public void performSearch(String query) {
        if (query.isEmpty()) {
            viewState.setValue(SearchViewState.errorBuilder()
                    .setErrorMessage("Search")
                    .setErrorIcon(R.drawable.ic_search_black_24dp)
                    .build());
        } else {
            viewState.setValue(SearchViewState.errorBuilder()
                    .setCurrentState(LoadingViewState.State.LOADING)
                    .build());
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(this::setResponse);
        }
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
