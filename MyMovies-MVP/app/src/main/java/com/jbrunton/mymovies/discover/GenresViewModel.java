package com.jbrunton.mymovies.discover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.jbrunton.mymovies.BaseViewModel;
import com.jbrunton.mymovies.Movie;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.repositories.GenresRepository;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.converters.GenresConverter;
import com.jbrunton.mymovies.models.Genre;

import java.util.List;

public class GenresViewModel extends BaseViewModel {
    private final MutableLiveData<GenresViewState> viewState = new MutableLiveData<>();
    private final GenresRepository repository;
    private final GenresConverter converter = new GenresConverter();

    public GenresViewModel() {
        repository = new GenresRepository(ServiceFactory.instance());
        repository.genres()
                .compose(applySchedulers())
                .subscribe(this::setResponse);
    }

    public LiveData<GenresViewState> viewState() {
        return viewState;
    }

    private void setResponse(MaybeError<List<Genre>> response) {
        response.ifValue(this::setGenresResponse).elseIfError(this::setErrorResponse);
    }

    private void setGenresResponse(List<Genre> genres) {
        viewState.setValue(converter.convertGenresResponse(genres));
    }

    private void setErrorResponse(DescriptiveError error) {
        viewState.setValue(converter.convertErrorResponse(error));
    }


}