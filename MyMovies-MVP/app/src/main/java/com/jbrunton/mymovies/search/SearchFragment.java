package com.jbrunton.mymovies.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbrunton.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SearchFragment extends Fragment {
    private SearchResultsAdapter searchResultsAdapter;
    @BindView(R.id.movies_list) RecyclerView moviesList;
    @BindView(R.id.search_query) EditText searchQuery;
    @BindView(R.id.error_case) View errorCase;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.error_image) ImageView errorImage;
    @BindView(R.id.error_try_again) Button errorTryAgainButton;
    private SearchViewModel viewModel;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        moviesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchResultsAdapter = new SearchResultsAdapter(getActivity());
        moviesList.setAdapter(searchResultsAdapter);

        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar myToolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);

        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        viewModel.viewState().observe(this, this::updateView);
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

    protected int toVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }

}
