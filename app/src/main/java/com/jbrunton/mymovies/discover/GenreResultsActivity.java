package com.jbrunton.mymovies.discover;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jbrunton.mymovies.BaseActivity;
import com.jbrunton.mymovies.LoadingStateContext;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.search.SearchResultsAdapter;
import com.jbrunton.mymovies.search.SearchViewState;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreResultsActivity extends BaseActivity {

    private SearchResultsAdapter moviesAdapter;
    @BindView(R.id.movies_list) RecyclerView moviesList;
    private GenreResultsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_results);

        ButterKnife.bind(this);
        bindErrorStateContext(new LoadingStateContext());

        moviesAdapter = new SearchResultsAdapter(this, R.layout.item_movie_card_list);
        moviesList.setAdapter(moviesAdapter);
        moviesList.setLayoutManager(new LinearLayoutManager(this));

        GenreResultsViewModel.Factory factory = new GenreResultsViewModel.Factory(
                getIntent().getExtras().getString("GENRE_ID"));
        viewModel = ViewModelProviders.of(this, factory)
                .get(GenreResultsViewModel.class);
        viewModel.viewState().observe(this, this::updateView);
    }

    private void updateView(SearchViewState viewState) {
        super.updateView(viewState);
        moviesList.setVisibility(toVisibility(!viewState.showError()));
        moviesAdapter.setDataSource(viewState.movies());
    }

    protected int toVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }
}
