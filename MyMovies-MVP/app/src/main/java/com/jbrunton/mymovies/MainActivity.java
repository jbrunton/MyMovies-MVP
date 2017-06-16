package com.jbrunton.mymovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createService().getMovie().enqueue(new Callback<MovieResource>() {
            @Override
            public void onResponse(Call<MovieResource> call, Response<MovieResource> response) {
                Toast.makeText(MainActivity.this, response.body().toMovie().getTitle(), Toast.LENGTH_LONG).show();
            }

            @Override public void onFailure(Call<MovieResource> call, Throwable t) {

            }
        });
    }

    private MovieService createService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MovieService.class);
    }
}
