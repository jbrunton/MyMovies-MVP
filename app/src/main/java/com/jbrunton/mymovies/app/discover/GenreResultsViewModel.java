package com.jbrunton.mymovies.app.discover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.app.search.SearchViewState;
import com.jbrunton.mymovies.app.search.SearchViewStateFactory;
import com.jbrunton.mymovies.app.shared.BaseViewModel;
import com.jbrunton.mymovies.app.shared.LoadingViewState;
import com.jbrunton.mymovies.models.Movie;

import java.util.Collections;
import java.util.List;

public class GenreResultsViewModel extends BaseViewModel {
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final SearchViewStateFactory viewStateFactory = new SearchViewStateFactory();

    public GenreResultsViewModel(String genreId) {
        repository = new MoviesRepository(ServiceFactory.instance());
        viewState.setValue(SearchViewState.builder()
                .setLoadingViewState(LoadingViewState.LOADING_STATE)
                .setMovies(Collections.emptyList())
                .build());
        repository.discoverByGenre(genreId)
                .compose(applySchedulers())
                .subscribe(this::setMoviesResponse, this::setErrorResponse);
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    private void setMoviesResponse(List<Movie> movies) {
        viewState.setValue(viewStateFactory.fromList(movies));
    }

    private void setErrorResponse(Throwable throwable) {
        viewState.setValue(viewStateFactory.fromError(DescriptiveError.from(throwable)));
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

       private final String genreId;

        public Factory(String genreId) {
            this.genreId = genreId;
        }

        @Override public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new GenreResultsViewModel(genreId);
        }
    }
}