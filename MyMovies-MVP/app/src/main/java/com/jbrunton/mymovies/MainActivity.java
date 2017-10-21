package com.jbrunton.mymovies;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbrunton.mymovies.search.SearchViewModel;
import com.jbrunton.mymovies.search.SearchViewState;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class MainActivity extends BaseActivity {
    private RecyclerView moviesList;
    private MoviesAdapter moviesAdapter;
    @BindView(R.id.search_query) EditText searchQuery;
    @BindView(R.id.error_case) View errorCase;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.error_image) ImageView errorImage;
    @BindView(R.id.error_try_again) Button errorTryAgainButton;
    private SearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setTitle("Search Movies");

        moviesList = (RecyclerView) findViewById(R.id.movies_list);
        //moviesList.setLayoutManager(new GridLayoutManager(this, 2));
        moviesList.setLayoutManager(new LinearLayoutManager(this));

        moviesAdapter = new MoviesAdapter(this);
        moviesList.setAdapter(moviesAdapter);

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
        moviesAdapter.setDataSource(viewState.movies());

        errorCase.setVisibility(toVisibility(viewState.showError()));
        errorText.setText(viewState.errorMessage());
        errorImage.setImageResource(viewState.errorIcon());
        errorTryAgainButton.setVisibility(toVisibility(viewState.showTryAgainButton()));
    }

    private int toVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }

    private static class MoviesAdapter extends BaseRecyclerAdapter<Movie, MoviesAdapter.ViewHolder> {
        MoviesAdapter(Context context) {
            super(R.layout.item_movie_card, new ViewHolderFactory(context));
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private TextView titleView;
            private TextView releaseDateView;
            private ImageView poster;
            private TextView ratingView;

            ViewHolder(View itemView) {
                super(itemView);
                titleView = (TextView) itemView.findViewById(R.id.title);
                releaseDateView = (TextView) itemView.findViewById(R.id.release_date);
                ratingView = (TextView) itemView.findViewById(R.id.rating);
                poster = (ImageView) itemView.findViewById(R.id.poster);
            }
        }

        protected static class ViewHolderFactory implements BaseRecyclerAdapter.ViewHolderFactory<Movie, ViewHolder> {
            private final Context context;

            public ViewHolderFactory(Context context) {
                this.context = context;
            }

            @Override
            public ViewHolder createViewHolder(View view) {
                return new ViewHolder(view);
            }

            @Override
            public void bindHolder(ViewHolder holder, Movie item) {
                holder.titleView.setText(item.getTitle());
                if (item.getReleaseDate().isPresent()) {
                    holder.releaseDateView.setText("" + item.getReleaseDate().get().getYear());
                } else {
                    holder.releaseDateView.setText("");
                }
                holder.ratingView.setText(Html.fromHtml("&#9734; " + item.getRating(), FROM_HTML_MODE_COMPACT));
                Picasso.with(context)
                        .load("http://image.tmdb.org/t/p/w300" + item.getPosterPath())
                        .resize(185, 275)
                        .centerCrop()
                        .into(holder.poster);
            }
        }
    }
}
