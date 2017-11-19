package com.jbrunton.mymovies.app.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.app.shared.BaseViewModel;
import com.jbrunton.mymovies.models.Movie;

import java.util.List;

public class SearchViewModel extends BaseViewModel {
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final SearchViewStateFactory viewStateFactory = new SearchViewStateFactory();

    public SearchViewModel() {
        repository = new MoviesRepository(ServiceFactory.instance());
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    @Override public void start() {
        viewState.setValue(viewStateFactory.searchEmptyState());
    }

    public void performSearch(String query) {
        if (query.isEmpty()) {
            viewState.setValue(viewStateFactory.searchEmptyState());
        } else {
            viewState.setValue(viewStateFactory.loadingState());
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(this::setMoviesResponse, this::setErrorResponse);
        }
    }

    private void setMoviesResponse(List<Movie> movies) {
        viewState.setValue(viewStateFactory.fromList(movies));
    }

    private void setErrorResponse(Throwable throwable) {
        viewState.setValue(viewStateFactory.fromError(throwable));
    }
}
