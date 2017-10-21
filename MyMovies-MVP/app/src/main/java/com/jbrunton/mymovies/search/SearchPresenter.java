package com.jbrunton.mymovies.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.DrawableRes;

import com.jbrunton.mymovies.MainActivity;
import com.jbrunton.mymovies.Movie;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;

import java.util.Collections;
import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter {
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    public SearchPresenter() {
        repository = new MoviesRepository(ServiceFactory.instance());
    }

    public void performSearch(String query) {
        if (query.isEmpty()) {
            viewState.setValue(new SearchViewState(Collections.emptyList(), true, "Search", R.drawable.ic_search_black_24dp, false));
        } else {
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(this::setResponse);
        }
    }

    private void setResponse(MaybeError<List<Movie>> response) {
        response.ifValue(this::setMoviesResponse).elseIfError(this::setErrorResponse);
    }

    private void setMoviesResponse(List<Movie> movies) {
        viewState.setValue(convertMoviesResponse(movies));
    }

    private void setErrorResponse(DescriptiveError error) {
        viewState.setValue(convertErrorResponse(error));
    }

    private SearchViewState convertMoviesResponse(List<Movie> movies) {
        if (movies.isEmpty()) {
            return new SearchViewState(movies, true, "No Results", R.drawable.ic_search_black_24dp, false);
        } else {
            return new SearchViewState(movies, false, "", 0, false);
        }
    }

    private SearchViewState convertErrorResponse(DescriptiveError error) {
        @DrawableRes int resId = error.isNetworkError() ? R.drawable.ic_sentiment_dissatisfied_black_24dp : R.drawable.ic_sentiment_very_dissatisfied_black_24dp;
        return new SearchViewState(Collections.emptyList(), true, error.getMessage(), resId, true);
    }

    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
