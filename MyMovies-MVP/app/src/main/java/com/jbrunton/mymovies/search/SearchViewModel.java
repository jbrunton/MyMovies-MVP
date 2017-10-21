package com.jbrunton.mymovies.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.DrawableRes;
import android.text.Html;
import android.view.View;

import com.jbrunton.mymovies.Movie;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class SearchViewModel extends ViewModel {
    private final MutableLiveData<SearchViewState> viewState = new MutableLiveData<>();
    private final MoviesRepository repository;

    public SearchViewModel() {
        repository = new MoviesRepository(ServiceFactory.instance());
    }

    public LiveData<SearchViewState> viewState() {
        return viewState;
    }

    public void performSearch(String query) {
        if (query.isEmpty()) {
            viewState.setValue(errorBuilder()
                    .setErrorMessage("Search")
                    .setErrorIcon(R.drawable.ic_search_black_24dp)
                    .build());
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
            return errorBuilder()
                    .setErrorMessage("No Results")
                    .setErrorIcon(R.drawable.ic_search_black_24dp)
                    .build();
        } else {
            return SearchViewState.builder()
                    .setShowError(false)
                    .setMovies(movies.stream().map(this::convertMovie).collect(Collectors.toList()))
                    .build();
        }
    }

    private SearchViewState convertErrorResponse(DescriptiveError error) {
        @DrawableRes int resId = error.isNetworkError() ? R.drawable.ic_sentiment_dissatisfied_black_24dp : R.drawable.ic_sentiment_very_dissatisfied_black_24dp;
        return errorBuilder()
                .setErrorMessage(error.getMessage())
                .setErrorIcon(resId)
                .setShowTryAgainButton(true)
                .build();
    }

    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private SearchViewState.Builder errorBuilder() {
        return SearchViewState.builder()
                .setMovies(Collections.emptyList())
                .setShowError(true);
    }

    private SearchItemViewState convertMovie(Movie movie) {
        return SearchItemViewState.builder()
                .setTitle(movie.getTitle())
                .setYearReleased(convertReleaseDate(movie.getReleaseDate()))
                .setRating("&#9734; " + movie.getRating())
                .setPosterUrl("http://image.tmdb.org/t/p/w300" + movie.getPosterPath())
                .build();
    }

    private String convertReleaseDate(Optional<LocalDate> date) {
        if (date.isPresent()) {
            return Integer.toString(date.get().getYear());
        } else {
            return "";
        }
    }
}
