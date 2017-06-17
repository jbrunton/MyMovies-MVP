package com.jbrunton.mymovies;

import android.os.Bundle;
import android.widget.TextView;

import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ApiKeyInterceptor;
import com.jbrunton.mymovies.api.services.MovieService;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {
    private TextView text;
    private TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text);
        text.setText("Loading...");
        text2 = (TextView) findViewById(R.id.text2);
        text2.setText("Loading...");

        MoviesRepository repository = new MoviesRepository(createService());
        repository.getMovie()
                .compose(applySchedulers())
                .subscribe(this::setMovie);
        repository.searchMovies("Star Trek")
                .compose(applySchedulers())
                .subscribe(this::setMovies);
    }

    private MovieService createService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ApiKeyInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(MovieService.class);
    }

    private void setMovie(Movie movie) {
        text.setText(movie.getTitle());
    }

    private void setMovies(List<Movie> movies) {
        text2.setText(movies.get(0).getTitle());
    }
}
