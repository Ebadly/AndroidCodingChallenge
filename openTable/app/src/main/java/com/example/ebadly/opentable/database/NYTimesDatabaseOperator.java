package com.example.ebadly.opentable.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.example.ebadly.opentable.dataModels.Movie;
import com.example.ebadly.opentable.dataModels.Multimedia;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;


/**
 * This class is used to perform all and any database operations
 */
public class NYTimesDatabaseOperator implements NYTimesDB {

    static final String DATABASE_NAME = "MovieDatabase.db";
    static final int DATABASE_VERSION = 1;

    //might not need this variable
    public static final int NAME_COLUMN = 1;

    // Variable to hold the database instance
    public static SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private static NYTimesDBHelper dbHelper;

    public NYTimesDatabaseOperator(Context context) {
        this.context = context;
        this.dbHelper = new NYTimesDBHelper(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    private void openDatabase(){ db = dbHelper.getWritableDatabase(); }

    private void closeDatabase(){ db.close(); }

    /**
     * cache a movie
     * @param movie movie that should be cached
     * @return true if successfull save
     */
    @Override
    public boolean insertMovie(@NonNull Movie movie){

        if(movie == null) return false;

        try {
            ContentValues contentValues = createContentValues(movie);
            openDatabase();
            db.insert(DatabaseKeys.MOVIE_TABLE_NAME,null,contentValues);

        }catch (Exception e){
            closeDatabase();
            e.printStackTrace();
            return false;
        }

        closeDatabase();
        return true;
    }

    //helper method to reduce repeated code
    private ContentValues createContentValues(Movie movie){
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseKeys.COLUMN_DISPLAY_TITLE, movie.getDisplay_title());
        contentValues.put(DatabaseKeys.COLUMN_MPAA_RATING, movie.getMpaa_rating());
        contentValues.put(DatabaseKeys.COLUMN_CRITICS_PICK, movie.getCritics_pick());
        contentValues.put(DatabaseKeys.COLUMN_BY_LINE, movie.getByline());
        contentValues.put(DatabaseKeys.COLUMN_HEADLINE, movie.getHeadline());
        contentValues.put(DatabaseKeys.COLUMN_SUMMARY_SHORT, movie.getSummary_short());
        contentValues.put(DatabaseKeys.COLUMN_PUBLICATION_DATE, movie.getPublication_date());
        contentValues.put(DatabaseKeys.COLUMN_OPENING_DATE, movie.getOpening_date());
        contentValues.put(DatabaseKeys.COLUMN_UPDATED_DATE, movie.getDate_updated());

        Multimedia multimedia = movie.getMultimedia();
        contentValues.put(DatabaseKeys.COLUMN_MEDIA_TYPE, multimedia.getType());
        contentValues.put(DatabaseKeys.COLUMN_MEDIA_SRC, multimedia.getSrc());
        contentValues.put(DatabaseKeys.COLUMN_MEDIA_WIDTH, multimedia.getWidth());
        contentValues.put(DatabaseKeys.COLUMN_MEDIA_HEIGHT, multimedia.getHeight());

        return contentValues;
    }

    /**
     *
     * @param movies list of cached movie list that should be cached
     * @return  true if successfull save
     */
    @Override
    public boolean insertMovieList(@NonNull List<Movie> movies){

        if(movies.size() == 0) return false;

        try {
            openDatabase();
            for(Movie movie : movies){
                if(movie == null) continue;
                ContentValues contentValues = createContentValues(movie);
                db.insert(DatabaseKeys.MOVIE_TABLE_NAME,null,contentValues);
            }
            closeDatabase();

        }catch (Exception e){
            e.printStackTrace();
            closeDatabase();
            return false;
        }

        return true;
    }

    /**
     * Hopefully we have cached a certain movie, if so,
     * then return that movie from the data base;
     *
     * @param movieName should be the display name of the movie
     * @return a movie that has been previously cached
     */
    @Override
    public Observable<Movie> getMovie(@NonNull String movieName){

        Movie movie = new Movie();

        if(TextUtils.isEmpty(movieName)) return null;

        try{

            openDatabase();

            String where = DatabaseKeys.COLUMN_DISPLAY_TITLE + "=?";
            Cursor cursor =
                    db.query(DatabaseKeys.MOVIE_TABLE_NAME, null, where,
                            new String[]{movieName},
                            null, null,null);

            if (cursor.getCount() < 1) return null;
            cursor.moveToFirst();

            movie = createMovie(cursor);
            closeDatabase();

        }catch (Exception e){
            e.printStackTrace();
            closeDatabase();
            return null;
        }
        return Observable.just(movie);
    }

    // helper method to help reduce code
    private Movie createMovie(Cursor cursor){

        Movie movie = new Movie();

        int byLineCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_BY_LINE);
        movie.setByline(cursor.getString(byLineCol));

        int displayTitleCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_DISPLAY_TITLE);
        movie.setDisplay_title(cursor.getString(displayTitleCol));

        int mpaaCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_MPAA_RATING);
        movie.setMpaa_rating(cursor.getString(mpaaCol));

        int criticsPickCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_CRITICS_PICK);
        movie.setCritics_pick(cursor.getInt(criticsPickCol));

        int headLineCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_HEADLINE);
        movie.setHeadline(cursor.getString(headLineCol));

        int shortSummaryCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_SUMMARY_SHORT);
        movie.setSummary_short(cursor.getString(shortSummaryCol));

        int pubDateCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_PUBLICATION_DATE);
        movie.setPublication_date(cursor.getString(pubDateCol));

        int openDatCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_OPENING_DATE);
        movie.setOpening_date(cursor.getString(openDatCol));

        int dateUpdateCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_UPDATED_DATE);
        movie.setDate_updated(cursor.getString(dateUpdateCol));

        Multimedia multimedia = new Multimedia();

        int mediaTypeCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_MEDIA_TYPE);
        multimedia.setType(cursor.getString(mediaTypeCol));

        int mediaSrcCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_MEDIA_SRC);
        multimedia.setSrc(cursor.getString(mediaSrcCol));

        int mediaWidthCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_MEDIA_WIDTH);
        multimedia.setWidth(Integer.parseInt(cursor.getString(mediaWidthCol)));

        int mediaHeightCol = cursor.getColumnIndex(DatabaseKeys.COLUMN_MEDIA_HEIGHT);
        multimedia.setHeight(Integer.parseInt(cursor.getString(mediaHeightCol)));

        movie.setMultimedia(multimedia);

        return movie;
    }

    /**
     * Hopefully we have saved a list of movies, if so,
     * this method will return that list
     * @return list of cached movies
     */
    @Override
    public Observable<List<Movie>> getMovieList(){

        List<Movie> movies = new ArrayList<Movie>();

        try {

            openDatabase();
            String sql = "SELECT * FROM " + DatabaseKeys.MOVIE_TABLE_NAME;
            Cursor cursor = db.rawQuery(sql,null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Movie movie = createMovie(cursor);
                    movies.add(movie);
                    cursor.moveToNext();
                }
            }

            closeDatabase();

        }catch (Exception e){
            e.printStackTrace();
            closeDatabase();
            return null;
        }

        return Observable.just(movies);
    }

    //implement if needed
    @Override
    public boolean deleteMovie(@NonNull Movie movie){
        return true;
    }

    //implement if needed
    @Override
    public boolean deleteMovieList(){
        return true;
    }
}
