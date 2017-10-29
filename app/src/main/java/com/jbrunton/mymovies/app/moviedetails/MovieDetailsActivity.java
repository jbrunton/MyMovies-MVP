package com.jbrunton.mymovies.app.moviedetails;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.app.helpers.PicassoHelper;
import com.jbrunton.mymovies.app.shared.BaseActivity;
import com.jbrunton.mymovies.app.shared.LoadingStateContext;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailsActivity extends BaseActivity {

    private MovieDetailsViewModel viewModel;
    private PicassoHelper picassoHelper = new PicassoHelper();
    @BindView(R.id.overview) TextView overview;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.content) View content;
    @BindView(R.id.backdrop) ImageView backdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        bindErrorStateContext(new LoadingStateContext());

        ButterKnife.bind(this);

        MovieDetailsViewModel.Factory factory = new MovieDetailsViewModel.Factory(
                getIntent().getExtras().getString("MOVIE_ID"));
        viewModel = ViewModelProviders.of(this, factory)
                .get(MovieDetailsViewModel.class);
        viewModel.viewState().observe(this, this::updateView);
    }

    @OnClick(R.id.error_try_again) public void tryAgain() {
        viewModel.loadDetails();
    }

    private void updateView(MovieDetailsViewState viewState) {
        updateLoadingView(viewState.loadingViewState());

        setTitle(viewState.movie().title());
        overview.setText(viewState.movie().overview());

        picassoHelper.loadImage(this, backdrop, viewState.movie().backdropUrl());
    }
}
