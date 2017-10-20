package com.jbrunton.mymovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class MainActivity extends BaseActivity {
    private RecyclerView moviesList;
    private MoviesAdapter moviesAdapter;
    @BindView(R.id.search_query) EditText searchQuery;
    @BindView(R.id.error_case) View errorCase;
    @BindView(R.id.error_text) TextView errorText;
    @BindView(R.id.error_image) ImageView errorImage;
    private MoviesRepository repository;

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

        repository = new MoviesRepository(ServiceFactory.instance());

        ButterKnife.bind(this);

        searchQuery.setText("Star Trek");
    }

    @OnTextChanged(R.id.search_query)
    public void onQueryChanged(CharSequence text) {
        String query = text.toString();
        if (query.isEmpty()) {
            showError("Search", R.drawable.ic_search_black_24dp);
        } else {
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(MainActivity.this::updateView);
        }
    }

    private void updateView(MaybeError<List<Movie>> movies) {
        movies.ifValue(this::showMovies).elseIfError(this::showError);
    }

    private void showMovies(List<Movie> movies) {
        if (movies.isEmpty()) {
            showError("No Results", R.drawable.ic_search_black_24dp);
        } else {
            errorCase.setVisibility(View.GONE);
            moviesList.setVisibility(View.VISIBLE);
            moviesAdapter.setDataSource(movies);
        }
    }

    private void showError(DescriptiveError error) {
        showError(error.getMessage(),
                error.isNetworkError() ? R.drawable.ic_sentiment_dissatisfied_black_24dp : R.drawable.ic_sentiment_very_dissatisfied_black_24dp);
    }

    private void showError(String text, @DrawableRes int resId) {
        moviesList.setVisibility(View.GONE);
        errorCase.setVisibility(View.VISIBLE);
        errorText.setText(text);
        errorImage.setImageResource(resId);
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
