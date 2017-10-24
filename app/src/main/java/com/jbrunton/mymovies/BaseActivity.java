package com.jbrunton.mymovies;

import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseActivity extends AppCompatActivity {
    private LoadingStateContext loadingStateContext;

    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected void bindErrorStateContext(LoadingStateContext loadingStateContext) {
        this.loadingStateContext = loadingStateContext;
        ButterKnife.bind(loadingStateContext, findViewById(android.R.id.content));
    }

    protected void updateView(LoadingViewState viewState) {
        loadingStateContext.updateView(viewState);
    }
}
