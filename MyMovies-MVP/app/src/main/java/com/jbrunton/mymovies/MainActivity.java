package com.jbrunton.mymovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ApiKeyInterceptor;
import com.jbrunton.mymovies.api.services.LocalDateAdapter;
import com.jbrunton.mymovies.api.services.MovieService;
import com.jbrunton.mymovies.api.services.ServiceFactory;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class MainActivity extends BaseActivity {
    private RecyclerView moviesList;
    private MoviesAdapter moviesAdapter;
    @BindView(R.id.search_query) EditText searchQuery;
    @BindView(R.id.empty_case) View emptyCase;
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
            emptyCase.setVisibility(View.VISIBLE);
            moviesList.setVisibility(View.GONE);
        } else {
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(MainActivity.this::setMovies);
        }
    }

    private void setMovies(MaybeError<List<Movie>> movies) {
        emptyCase.setVisibility(View.GONE);
        moviesList.setVisibility(View.VISIBLE);
        movies.ifValue(moviesAdapter::setDataSource).elseIfError(this::showError);
    }

    private void showError(DescriptiveError error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
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
