package com.jbrunton.mymovies.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jbrunton.mymovies.LoadingStateContext;
import com.jbrunton.mymovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.jbrunton.mymovies.converters.VisibilityConverter.toVisibility;

public class SearchFragment extends Fragment {
    private SearchResultsAdapter searchResultsAdapter;
    @BindView(R.id.movies_list) RecyclerView moviesList;
    @BindView(R.id.search_query) EditText searchQuery;
    private final LoadingStateContext loadingStateContext = new LoadingStateContext();
    private SearchViewModel viewModel;

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable performSearch;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);
        ButterKnife.bind(this.loadingStateContext, view);

        moviesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchResultsAdapter = new SearchResultsAdapter(getActivity(), R.layout.item_movie_card_list);
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
        handler.removeCallbacks(performSearch);
        performSearch = () -> viewModel.performSearch(text.toString());
        handler.postDelayed(performSearch, 500);
    }

    @OnClick(R.id.error_try_again)
    public void onTryAgain() {
        viewModel.performSearch(searchQuery.getText().toString());
    }

    private void updateView(SearchViewState viewState) {
        moviesList.setVisibility(toVisibility(viewState.showContent()));
        searchResultsAdapter.setDataSource(viewState.movies());
        loadingStateContext.updateView(viewState);
    }
}
