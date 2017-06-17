package com.jbrunton.mymovies;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.api.services.ApiKeyInterceptor;
import com.jbrunton.mymovies.api.services.MovieService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {
    private RecyclerView moviesList;
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesList = (RecyclerView) findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new LinearLayoutManager(this));

        moviesAdapter = new MoviesAdapter();
        moviesList.setAdapter(moviesAdapter);

        MoviesRepository repository = new MoviesRepository(createService());
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

    private void setMovies(List<Movie> movies) {
        moviesAdapter.setDataSource(movies);
    }

    private static class MoviesAdapter extends BaseRecyclerAdapter<Movie, MoviesAdapter.ViewHolder> {
        MoviesAdapter() {
            super(android.R.layout.simple_list_item_1, new ViewHolderFactory());
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }

        protected static class ViewHolderFactory implements BaseRecyclerAdapter.ViewHolderFactory<Movie, ViewHolder> {
            @Override
            public ViewHolder createViewHolder(View view) {
                return new ViewHolder(view);
            }

            @Override
            public void bindHolder(ViewHolder holder, Movie item) {
                holder.textView.setText(item.getTitle());
            }
        }
    }
}
