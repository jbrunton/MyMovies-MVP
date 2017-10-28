package com.jbrunton.mymovies.app.discover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jbrunton.mymovies.app.shared.BaseViewModel;
import com.jbrunton.mymovies.app.shared.LoadingViewState;
import com.jbrunton.mymovies.models.Movie;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.app.converters.MovieResultsConverter;
import com.jbrunton.mymovies.app.search.SearchViewState;

import java.util.Collections;
import java.util.List;

public class GenreResultsViewModel extends BaseViewModel {
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;
    private final MovieResultsConverter converter = new MovieResultsConverter();

    public GenreResultsViewModel(String genreId) {
        repository = new MoviesRepository(ServiceFactory.instance());
        viewState.setValue(SearchViewState.builder()
                .setLoadingViewState(LoadingViewState.LOADING_STATE)
                .setMovies(Collections.emptyList())
                .build());
        repository.discoverByGenre(genreId)
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