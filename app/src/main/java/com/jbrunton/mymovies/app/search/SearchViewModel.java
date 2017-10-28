package com.jbrunton.mymovies.app.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.jbrunton.mymovies.app.shared.BaseViewModel;
import com.jbrunton.mymovies.app.shared.LoadingViewState;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.app.converters.MovieResultsConverter;

public class SearchViewModel extends BaseViewModel {
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final MovieResultsConverter converter = new MovieResultsConverter();

    public SearchViewModel() {
        repository = new MoviesRepository(ServiceFactory.instance());
        viewState.setValue(converter.emptySearchViewState(
                LoadingViewState.errorBuilder()
                        .setErrorMessage("Search")
                        .setErrorIcon(R.drawable.ic_search_black_24dp)
                        .build()));
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    public void performSearch(String query) {
        if (query.isEmpty()) {
            viewState.setValue(converter.emptySearchViewState(
                    LoadingViewState.errorBuilder()
                        .setErrorMessage("Search")
                        .setErrorIcon(R.drawable.ic_search_black_24dp)
                        .build()));
        } else {
            viewState.setValue(converter.emptySearchViewState(LoadingViewState.LOADING_STATE));
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(response -> response.ifValue(movies -> viewState.setValue(converter.toSearchViewState(movies))).elseIfError(error -> viewState.setValue(converter.toSearchViewState(error))));
        }
    }


}
