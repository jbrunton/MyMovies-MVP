package com.jbrunton.mymovies.discover;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.search.SearchResultsAdapter;
import com.jbrunton.mymovies.search.SearchViewModel;
import com.jbrunton.mymovies.search.SearchViewState;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverFragment extends Fragment {
    private SearchResultsAdapter nowPlayingAdapter;
    private DiscoverViewModel viewModel;
    @BindView(R.id.now_playing) RecyclerView nowPlayingList;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, view);

        nowPlayingList.setLayoutManager(new LinearLayoutManager(getActivity()));
        nowPlayingAdapter = new SearchResultsAdapter(getActivity());
        nowPlayingList.setAdapter(nowPlayingAdapter);

        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(DiscoverViewModel.class);
        viewModel.viewState().observe(this, this::updateView);
    }

    private void updateView(SearchViewState viewState) {
        nowPlayingList.setVisibility(toVisibility(!viewState.showError()));
        nowPlayingAdapter.setDataSource(viewState.movies());

//        errorCase.setVisibility(toVisibility(viewState.showError()));
//        errorText.setText(viewState.errorMessage());
//        errorImage.setImageResource(viewState.errorIcon());
//        errorTryAgainButton.setVisibility(toVisibility(viewState.showTryAgainButton()));
    }

    protected int toVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }
}
