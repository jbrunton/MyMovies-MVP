package com.jbrunton.mymovies.discover;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.search.SearchResultsAdapter;
import com.jbrunton.mymovies.search.SearchViewModel;
import com.jbrunton.mymovies.search.SearchViewState;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreResultsActivity extends AppCompatActivity {

    private SearchResultsAdapter moviesAdapter;
    @BindView(R.id.movies_list) RecyclerView moviesList;
    @BindView(R.id.error_case) View errorCase;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.error_image) ImageView errorImage;
    @BindView(R.id.error_try_again) Button errorTryAgainButton;
    private GenreResultsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_results);

        ButterKnife.bind(this);

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
        moviesList.setVisibility(toVisibility(!viewState.showError()));
        moviesAdapter.setDataSource(viewState.movies());

        errorCase.setVisibility(toVisibility(viewState.showError()));
        errorText.setText(viewState.errorMessage());
        errorImage.setImageResource(viewState.errorIcon());
        errorTryAgainButton.setVisibility(toVisibility(viewState.showTryAgainButton()));
    }

    protected int toVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }
}
