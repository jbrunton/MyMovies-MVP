package com.jbrunton.mymovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbrunton.mymovies.search.SearchResultsAdapter;
import com.jbrunton.mymovies.search.SearchViewModel;
import com.jbrunton.mymovies.search.SearchViewState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SearchActivity extends BaseActivity {
    private RecyclerView moviesList;
    private SearchResultsAdapter searchResultsAdapter;
    @BindView(R.id.search_query) EditText searchQuery;
    @BindView(R.id.error_case) View errorCase;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.error_image) ImageView errorImage;
    @BindView(R.id.error_try_again) Button errorTryAgainButton;
    private SearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setTitle("Search Movies");

        moviesList = (RecyclerView) findViewById(R.id.movies_list);
        //moviesList.setLayoutManager(new GridLayoutManager(this, 2));
        moviesList.setLayoutManager(new LinearLayoutManager(this));

        searchResultsAdapter = new SearchResultsAdapter(this);
        moviesList.setAdapter(searchResultsAdapter);

        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        viewModel.viewState().observe(this, this::updateView);

        ButterKnife.bind(this);

        searchQuery.setText("Star Trek");
    }

    @OnTextChanged(R.id.search_query)
    public void onQueryChanged(CharSequence text) {
        viewModel.performSearch(text.toString());
    }

    @OnClick(R.id.error_try_again)
    public void onTryAgain() {
        viewModel.performSearch(searchQuery.getText().toString());
    }

    private void updateView(SearchViewState viewState) {
        moviesList.setVisibility(toVisibility(!viewState.showError()));
        searchResultsAdapter.setDataSource(viewState.movies());

        errorCase.setVisibility(toVisibility(viewState.showError()));
        errorText.setText(viewState.errorMessage());
        errorImage.setImageResource(viewState.errorIcon());
        errorTryAgainButton.setVisibility(toVisibility(viewState.showTryAgainButton()));
    }

}
