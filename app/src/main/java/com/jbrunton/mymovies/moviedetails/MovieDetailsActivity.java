package com.jbrunton.mymovies.moviedetails;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jbrunton.mymovies.BaseActivity;
import com.jbrunton.mymovies.LoadingStateContext;
import com.jbrunton.mymovies.LoadingViewState;
import com.jbrunton.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends BaseActivity {

    private MovieDetailsViewModel viewModel;
    @BindView(R.id.overview) TextView overview;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.content) View content;

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

    private void updateView(MovieDetailsViewState viewState) {
        super.updateView(viewState);
        //content.setVisibility(toVisibility(viewState.showContent()));

        // TODO: use a null object here
        if (viewState.currentState() == LoadingViewState.State.OK) {
            setTitle(viewState.movie().get().title());
            overview.setText(viewState.movie().get().overview().get());
        }
    }
}
