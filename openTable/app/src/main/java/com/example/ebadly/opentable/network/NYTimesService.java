package com.example.ebadly.opentable.network;

import android.content.Context;
import com.example.ebadly.opentable.R;
import com.example.ebadly.opentable.dataModels.Movie;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.io.IOException;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NYTimesService {

    public enum OrderBy{
        BY_DATE,
    }

    public NYTimesAPI nyTimesAPI;
    private Context context;
    private String api_key;

    public NYTimesService(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.base_url))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nyTimesAPI = retrofit.create(NYTimesAPI.class);
        api_key = this.context.getResources().getString(R.string.api_key);
    }

    /**
     *
     * @param enumType we may want to add extra order options in the future
     * @return A list of Movies
     * @throws IOException
     */
    public Observable<List<Movie>> getMoviesList(OrderBy enumType) throws IOException {
        String orderBy = getOrderByParameter(enumType);
        List<Movie> movies = nyTimesAPI.getMoviesList(orderBy,api_key)
                .subscribeOn(Schedulers.io()).blockingFirst().getResults();
        return Observable.just(movies);
    }

    /**
     *
     * @return a list of Movies
     * @throws IOException
     */
    public Observable<List<Movie>> getMoviesList() throws IOException {

        String orderBy = ""; // order-by is empty, let server decide
        List<Movie> movies = nyTimesAPI.getMoviesList(orderBy,api_key)
                .subscribeOn(Schedulers.io())
                .blockingFirst().getResults();

        return Observable.just(movies);
    }

    /**
     *
     * Helper method that will return an order option based on the enum
     *
     * @param enumType we may want to add extra order options in the future
     * @return a String indicating an ordering option
     */
    private String getOrderByParameter(OrderBy enumType){
        switch (enumType){
            case BY_DATE:
                return this.context.getResources().getString(R.string.order_by_date);
            default:
                // return empty string, let the server decide on what to do with empty parameter
                return "";
        }
    }
}
