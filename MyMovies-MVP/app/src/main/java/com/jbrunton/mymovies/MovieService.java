package com.jbrunton.mymovies;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieService {
    @GET("movie/76341?api_key=5b07463cf2bdde60339c9621dcd6e226")
    Observable<MovieResource> getMovie();
}
