package com.example.ebadly.opentable.database;

import android.support.annotation.NonNull;
import com.example.ebadly.opentable.dataModels.Movie;
import java.util.List;
import io.reactivex.Observable;

/**
 * This class's purposes is cache the movie list
 * and also to retrieve the local/cached movie list
 */
public interface NYTimesDB {

    Observable<List<Movie>> getMovieList();

    Observable<Movie> getMovie(@NonNull String movieName);

    boolean insertMovieList(@NonNull List<Movie> movies);

    boolean insertMovie(@NonNull Movie movie);

    /**
     * Deletes all records and wipes the database clean
     * @return
     */
    boolean deleteMovieList();

    boolean deleteMovie(Movie movie);

}
