package com.jbrunton.mymovies;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseActivity extends AppCompatActivity {
    private ErrorStateContext errorStateContext;

    protected <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected void bindErrorStateContext(ErrorStateContext errorStateContext) {
        this.errorStateContext = errorStateContext;
        ButterKnife.bind(errorStateContext, findViewById(android.R.id.content));
    }

    protected void updateView(ErrorViewState viewState) {
        errorStateContext.updateView(viewState);
    }
}
