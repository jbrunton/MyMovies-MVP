package com.jbrunton.mymovies.search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.shared.BaseFragment;
import com.jbrunton.mymovies.shared.LoadingStateContext;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.jbrunton.mymovies.helpers.VisibilityConverter.toVisibility;

public class SearchFragment extends BaseFragment<SearchViewModel> {
    private SearchResultsAdapter searchResultsAdapter;
    @BindView(R.id.movies_list) RecyclerView moviesList;
    @BindView(R.id.search_query) EditText searchQuery;
    private final LoadingStateContext loadingStateContext = new LoadingStateContext();

    Handler handler = new Handler(Looper.getMainLooper());

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

        viewModel().viewState().observe(this, this::updateView);
    }

    @Override protected SearchViewModel provideViewModel() {
        SearchViewModel.Factory factory = new SearchViewModel.Factory(dependencies().getMoviesRepository());
        return getViewModel(SearchViewModel.class, factory);
    }

    @OnTextChanged(R.id.search_query)
    public void onQueryChanged(CharSequence text) {
        handler.removeCallbacks(this::performSearch);
        handler.postDelayed(this::performSearch, 500);
    }

    @OnClick(R.id.error_try_again)
    public void onTryAgain() {
        performSearch();
    }

    private void performSearch() {
        viewModel().performSearch(searchQuery.getText().toString());
    }

    public void updateView(SearchViewState viewState) {
        moviesList.setVisibility(toVisibility(viewState.loadingViewState().showContent()));
        searchResultsAdapter.setDataSource(viewState.movies());
        loadingStateContext.updateView(viewState.loadingViewState());
    }
}
