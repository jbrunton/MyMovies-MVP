package com.jbrunton.mymovies.app.shared;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jbrunton.mymovies.MyMoviesApplication;
import com.jbrunton.mymovies.app.ApplicationDependencies;

public abstract class BaseFragment<T extends BaseViewModel> extends Fragment {
    private T viewModel;

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = provideViewModel();
        viewModel.start();
    }

    protected ApplicationDependencies dependencies() {
        return ((MyMoviesApplication) getActivity().getApplication()).dependencies();
    }

    protected T viewModel() {
        return this.viewModel;
    }

    protected abstract T provideViewModel();

    protected T getViewModel(Class<T> modelClass, ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(this, factory).get(modelClass);
    }
}
