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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ApiKeyInterceptor;
import com.jbrunton.mymovies.api.services.LocalDateAdapter;
import com.jbrunton.mymovies.api.services.MovieService;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class MainActivity extends BaseActivity {
    private RecyclerView moviesList;
    private MoviesAdapter moviesAdapter;
    private EditText searchQuery;

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

        final MoviesRepository repository = new MoviesRepository(createService());

        searchQuery = (EditText) findViewById(R.id.search_query);
        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                repository.searchMovies(editable.toString())
                        .compose(applySchedulers())
                        .subscribe(MainActivity.this::setMovies);
            }
        });

        searchQuery.setText("Star Trek");
    }

    private MovieService createService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ApiKeyInterceptor())
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(MovieService.class);
    }

    private void setMovies(List<Movie> movies) {
        moviesAdapter.setDataSource(movies);
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
                holder.releaseDateView.setText("" + item.getReleaseDate().getYear());
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
