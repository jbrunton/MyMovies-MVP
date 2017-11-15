package com.jbrunton.mymovies.app.shared;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity {
    private LoadingStateContext loadingStateContext;
    private T viewModel;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = createViewModel();
        viewModel.start();
    }

    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected void bindErrorStateContext(LoadingStateContext loadingStateContext) {
        this.loadingStateContext = loadingStateContext;
        ButterKnife.bind(loadingStateContext, findViewById(android.R.id.content));
    }

    protected void updateLoadingView(LoadingViewState viewState) {
        loadingStateContext.updateView(viewState);
    }

    protected T viewModel() {
        return this.viewModel;
    }

    protected abstract T createViewModel();
}
