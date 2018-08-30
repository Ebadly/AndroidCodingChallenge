package com.example.ebadly.opentable.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTimesAPI {

    @GET("dvd-picks.json?")
    Observable<NYTimesAPIResponse> getMoviesList(@Query("order") String orderBy,
                                                 @Query("api-key") String apiKey);

}
