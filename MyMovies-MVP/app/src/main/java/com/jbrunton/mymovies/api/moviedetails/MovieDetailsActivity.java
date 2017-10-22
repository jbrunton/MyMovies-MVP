package com.jbrunton.mymovies.api.moviedetails;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jbrunton.mymovies.LoadingStateContext;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.discover.GenreResultsViewModel;
import com.jbrunton.mymovies.search.SearchResultsAdapter;
import com.jbrunton.mymovies.search.SearchViewState;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    private MovieDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        }
        //moviesList.setVisibility(toVisibility(!viewState.showError()));
        //moviesAdapter.setDataSource(viewState.movies());
    }
}
