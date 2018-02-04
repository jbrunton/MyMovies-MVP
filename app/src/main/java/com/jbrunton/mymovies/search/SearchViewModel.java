package com.jbrunton.mymovies.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jbrunton.entities.Movie;
import com.jbrunton.entities.MoviesRepository;
import com.jbrunton.mymovies.shared.BaseViewModel;

import java.util.List;

public class SearchViewModel extends BaseViewModel {
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final SearchViewStateFactory viewStateFactory = new SearchViewStateFactory();

    public SearchViewModel(MoviesRepository repository) {
        this.repository = repository;
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    @Override public void start() {
        viewState.setValue(viewStateFactory.getSearchEmptyState());
    }

    public void performSearch(String query) {
        if (query.isEmpty()) {
            viewState.postValue(viewStateFactory.getSearchEmptyState());
        } else {
            viewState.postValue(viewStateFactory.getLoadingState());
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(this::setMoviesResponse, this::setErrorResponse);
        }
    }

    private void setMoviesResponse(List<Movie> movies) {
        viewState.postValue(viewStateFactory.fromList(movies));
    }

    private void setErrorResponse(Throwable throwable) {
        viewState.postValue(viewStateFactory.fromError(throwable));
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final MoviesRepository repository;

        public Factory(MoviesRepository repository) {
            this.repository = repository;
        }

        @Override public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new SearchViewModel(repository);
        }
    }
}
