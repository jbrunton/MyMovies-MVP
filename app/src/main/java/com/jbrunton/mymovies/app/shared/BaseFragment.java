package com.jbrunton.mymovies.app.shared;

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

    protected T viewModel() {
        return this.viewModel;
    }

    protected abstract T provideViewModel();

    protected ApplicationDependencies dependencies() {
        return ((MyMoviesApplication) getActivity().getApplication()).dependencies();
    }
}
