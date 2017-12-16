package com.jbrunton.mymovies.discover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jbrunton.entities.MoviesRepository;
import com.jbrunton.networking.DescriptiveError;
import com.jbrunton.mymovies.search.SearchViewState;
import com.jbrunton.mymovies.search.SearchViewStateFactory;
import com.jbrunton.mymovies.shared.BaseViewModel;
import com.jbrunton.mymovies.shared.LoadingViewState;
import com.jbrunton.entities.Movie;

import java.util.Collections;
import java.util.List;

public class GenreResultsViewModel extends BaseViewModel {
    private final String genreId;
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final SearchViewStateFactory viewStateFactory = new SearchViewStateFactory();

    public GenreResultsViewModel(String genreId, MoviesRepository repository) {
        this.genreId = genreId;
        this.repository = repository;
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    @Override public void start() {
        viewState.setValue(SearchViewState.builder()
                .setLoadingViewState(LoadingViewState.LOADING_STATE)
                .setMovies(Collections.emptyList())
                .build());
        repository.discoverByGenre(genreId)
                .compose(applySchedulers())
                .subscribe(this::setMoviesResponse, this::setErrorResponse);
    }

    private void setMoviesResponse(List<Movie> movies) {
        viewState.setValue(viewStateFactory.fromList(movies));
    }

    private void setErrorResponse(Throwable throwable) {
        viewState.setValue(viewStateFactory.fromError(DescriptiveError.from(throwable)));
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

       private final String genreId;
       private final MoviesRepository repository;

        public Factory(String genreId, MoviesRepository repository) {
            this.genreId = genreId;
            this.repository = repository;
        }

        @Override public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new GenreResultsViewModel(genreId, repository);
        }
    }
}