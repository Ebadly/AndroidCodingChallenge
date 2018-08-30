package com.example.ebadly.opentable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.example.ebadly.opentable.dataModels.Movie;
import com.example.ebadly.opentable.database.NYTimesDatabaseOperator;
import com.example.ebadly.opentable.database.SharedPrefManager;
import com.example.ebadly.opentable.network.NYTimesService;
import java.io.IOException;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class MovieManager extends Thread {

    private NYTimesDatabaseOperator localOperator;
    private NYTimesService remoteOperator;

    //timer checker, will only make network calls based on a 1 min timer
    //otherwise grab from local database;
    private SharedPrefManager sharedPrefManager;

    public MovieManager(Context context) {
        this.localOperator = new NYTimesDatabaseOperator(context);
        this.remoteOperator = new NYTimesService(context);
        this.sharedPrefManager = new SharedPrefManager(context);
    }

    /**
     *
     * This method will check the data base first
     * if it doesn't exist, we will pull a new list from the server
     * and then retry getting the movie
     *
     * @param movie
     * @return movie
     */
    public Observable<Movie> getMovie(@NonNull Movie movie){

        String movieName = movie.getDisplay_title();

        if(!sharedPrefManager.isTimeToRefreshData() || TextUtils.isEmpty(movieName)) {
            Observable<Movie> movieObservable =
                    localOperator.getMovie(movieName).subscribeOn(Schedulers.io());
            return movieObservable;
        } else{
            sharedPrefManager.storeTimeCurrent();
            try {
                List<Movie> movies = remoteOperator.getMoviesList().blockingFirst();
                localOperator.insertMovieList(movies);
                for(int i = 0; i < movies.size(); i++){
                    Movie getMovie = movies.get(i);
                    if(movie.getDisplay_title().equals(getMovie.getDisplay_title())){
                        movie = getMovie;
                        return Observable.just(movie);
                    }
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * this method will check the data base for movie list, if nothing exists
     * then we will pull from server and then try again.
     * @return
     */
    public Observable<List<Movie>> getMovieList(){
        Observable<List<Movie>> moviesObservable;
        if(!sharedPrefManager.isTimeToRefreshData()){
            moviesObservable = localOperator.getMovieList().subscribeOn(Schedulers.io());
            return moviesObservable;
        }else{
            sharedPrefManager.storeTimeCurrent();
            try {
                moviesObservable = remoteOperator.getMoviesList();
                localOperator.insertMovieList(moviesObservable.blockingFirst());
                return moviesObservable;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public Observable<List<Movie>> getMovieList(NYTimesService.OrderBy order){
        Observable<List<Movie>> movies;
        if(!sharedPrefManager.isTimeToRefreshData()){
            movies = localOperator.getMovieList().subscribeOn(Schedulers.io());
            return movies;
        }else{
            sharedPrefManager.storeTimeCurrent();
            try {
                movies = remoteOperator.getMoviesList(order);
                localOperator.insertMovieList(movies.blockingFirst());
                return movies;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
