package com.jbrunton.mymovies.app.discover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.jbrunton.entities.Genre;
import com.jbrunton.entities.GenresRepository;
import com.jbrunton.mymovies.app.shared.BaseViewModel;

import java.util.List;

public class GenresViewModel extends BaseViewModel {
    private final MutableLiveData<GenresViewState> viewState = new MutableLiveData<>();
    private final GenresRepository repository;
    private final GenresViewStateFactory converter = new GenresViewStateFactory();

    public GenresViewModel(GenresRepository repository) {
        this.repository = repository;
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

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final GenresRepository repository;

        public Factory(GenresRepository repository) {
            this.repository = repository;
        }

        @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new GenresViewModel(repository);
        }
    }
}