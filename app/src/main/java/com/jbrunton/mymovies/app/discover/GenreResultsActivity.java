package com.jbrunton.mymovies.app.discover;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.app.search.SearchResultsAdapter;
import com.jbrunton.mymovies.app.search.SearchViewState;
import com.jbrunton.mymovies.app.shared.BaseActivity;
import com.jbrunton.mymovies.app.shared.LoadingStateContext;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreResultsActivity extends BaseActivity<GenreResultsViewModel> {

    private SearchResultsAdapter moviesAdapter;
    @BindView(R.id.movies_list) RecyclerView moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_results);

        ButterKnife.bind(this);
        bindErrorStateContext(new LoadingStateContext());

        moviesAdapter = new SearchResultsAdapter(this, R.layout.item_movie_card_list);
        moviesList.setAdapter(moviesAdapter);
        moviesList.setLayoutManager(new LinearLayoutManager(this));


        viewModel().viewState().observe(this, this::updateView);
    }

    @Override protected GenreResultsViewModel provideViewModel() {
        GenreResultsViewModel.Factory factory = new GenreResultsViewModel.Factory(
                getIntent().getExtras().getString("GENRE_ID"));
        return ViewModelProviders.of(this, factory)
                .get(GenreResultsViewModel.class);
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
