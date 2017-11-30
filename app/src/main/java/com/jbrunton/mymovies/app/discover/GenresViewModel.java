package com.jbrunton.mymovies.app.discover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.jbrunton.mymovies.api.repositories.GenresRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.jbrunton.mymovies.app.shared.BaseViewModel;
import com.jbrunton.entities.Genre;

import java.util.List;

public class GenresViewModel extends BaseViewModel {
    private final MutableLiveData<GenresViewState> viewState = new MutableLiveData<>();
    private final GenresRepository repository;
    private final GenresViewStateFactory converter = new GenresViewStateFactory();

    public GenresViewModel() {
        repository = new GenresRepository(ServiceFactory.instance());
    }

    public LiveData<GenresViewState> viewState() {
        return viewState;
    }

    @Override public void start() {
        repository.genres()
                .compose(applySchedulers())
                .subscribe(this::setGenresResponse, this::setErrorResponse);
    }

    private void setGenresResponse(List<Genre> genres) {
        viewState.setValue(converter.fromList(genres));
    }

    private void setErrorResponse(Throwable throwable) {
        viewState.setValue(converter.fromError(throwable));
    }


}