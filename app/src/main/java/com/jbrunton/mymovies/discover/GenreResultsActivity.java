package com.jbrunton.mymovies.discover;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.search.SearchResultsAdapter;
import com.jbrunton.mymovies.search.SearchViewState;
import com.jbrunton.mymovies.shared.BaseActivity;
import com.jbrunton.mymovies.shared.LoadingStateContext;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreResultsActivity extends BaseActivity<GenreResultsViewModel> {

    private SearchResultsAdapter moviesAdapter;
    @BindView(R.id.movies_list) RecyclerView moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        bindErrorStateContext(new LoadingStateContext());

        moviesAdapter = new SearchResultsAdapter(this, R.layout.item_movie_card_list);
        moviesList.setAdapter(moviesAdapter);
        moviesList.setLayoutManager(new LinearLayoutManager(this));


        viewModel().viewState().observe(this, this::updateView);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected GenreResultsViewModel provideViewModel() {
        GenreResultsViewModel.Factory factory = new GenreResultsViewModel.Factory(
                getIntent().getExtras().getString("GENRE_ID"), dependencies().getMoviesRepository());
        return getViewModel(GenreResultsViewModel.class, factory);
    }

    private void updateView(SearchViewState viewState) {
        updateLoadingView(viewState.loadingViewState());
        moviesList.setVisibility(toVisibility(!viewState.loadingViewState().showError()));
        moviesAdapter.setDataSource(viewState.movies());
    }

    protected int toVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }
}
