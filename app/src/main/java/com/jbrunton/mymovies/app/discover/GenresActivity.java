package com.jbrunton.mymovies.app.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jbrunton.entities.Genre;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.app.shared.BaseActivity;
import com.jbrunton.mymovies.app.shared.LoadingStateContext;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenresActivity extends BaseActivity<GenresViewModel> {

    private GenresAdapter genresAdapter;
    @BindView(R.id.genres_list) ListView genresList;
    @BindView(R.id.error_case) View errorCase;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.error_image) ImageView errorImage;
    @BindView(R.id.error_try_again) Button errorTryAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        bindErrorStateContext(new LoadingStateContext());

        genresAdapter = new GenresAdapter(this);
        genresList.setAdapter(genresAdapter);

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

    @Override protected GenresViewModel provideViewModel() {
        GenresViewModel.Factory factory = new GenresViewModel.Factory(dependencies().genresRepository());
        return getViewModel(GenresViewModel.class, factory);
    }

    private void updateView(GenresViewState viewState) {
        genresList.setVisibility(toVisibility(!viewState.loadingViewState().showError()));
        genresAdapter.addAll(viewState.genres());

        updateLoadingView(viewState.loadingViewState());
    }

    protected int toVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }

    protected static class GenresAdapter extends ArrayAdapter<Genre> {
        public GenresAdapter(@NonNull Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // If there's no view to re-use, inflate a brand new view for row
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            Genre genre = getItem(position);

            TextView genreName = (TextView) convertView.findViewById(android.R.id.text1);
            genreName.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), GenreResultsActivity.class);
                intent.putExtra("GENRE_ID", genre.id());
                getContext().startActivity(intent);
            });
            genreName.setText(genre.name());

            return convertView;
        }
    }
}
