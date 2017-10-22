package com.jbrunton.mymovies.discover;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.models.Genre;
import com.jbrunton.mymovies.search.SearchResultsAdapter;
import com.jbrunton.mymovies.search.SearchViewModel;
import com.jbrunton.mymovies.search.SearchViewState;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenresActivity extends AppCompatActivity {

    private GenresAdapter genresAdapter;
    @BindView(R.id.genres_list) ListView genresList;
    @BindView(R.id.error_case) View errorCase;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.error_image) ImageView errorImage;
    @BindView(R.id.error_try_again) Button errorTryAgainButton;
    private SearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

        ButterKnife.bind(this);

        genresAdapter = new GenresAdapter(this);
        genresList.setAdapter(genresAdapter);

        GenresViewModel viewModel = ViewModelProviders.of(this).get(GenresViewModel.class);
        viewModel.viewState().observe(this, this::updateView);
    }

    private void updateView(GenresViewState viewState) {
        genresList.setVisibility(toVisibility(!viewState.showError()));
        genresAdapter.addAll(viewState.genres());

        errorCase.setVisibility(toVisibility(viewState.showError()));
        errorText.setText(viewState.errorMessage());
        errorImage.setImageResource(viewState.errorIcon());
        errorTryAgainButton.setVisibility(toVisibility(viewState.showTryAgainButton()));
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

            TextView genreName = (TextView) convertView.findViewById(android.R.id.text1);
            genreName.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), GenreResultsActivity.class);
                getContext().startActivity(intent);
            });

            Genre genre = getItem(position);
            genreName.setText(genre.name());

            return convertView;
        }
    }
}
