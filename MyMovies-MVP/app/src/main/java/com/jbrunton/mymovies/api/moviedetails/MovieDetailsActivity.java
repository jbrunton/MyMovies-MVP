package com.jbrunton.mymovies.api.moviedetails;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jbrunton.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    private MovieDetailsViewModel viewModel;
    @BindView(R.id.overview) TextView overview;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");

        ButterKnife.bind(this);

        MovieDetailsViewModel.Factory factory = new MovieDetailsViewModel.Factory(
                getIntent().getExtras().getString("MOVIE_ID"));
        viewModel = ViewModelProviders.of(this, factory)
                .get(MovieDetailsViewModel.class);
        viewModel.viewState().observe(this, this::updateView);
    }

    private void updateView(MovieDetailsViewState viewState) {
        if (viewState.movie().isPresent()) {
            setTitle(viewState.movie().get().title());
            overview.setText(viewState.movie().get().overview().get());
        }
        //moviesList.setVisibility(toVisibility(!viewState.showError()));
        //moviesAdapter.setDataSource(viewState.movies());
    }
}
