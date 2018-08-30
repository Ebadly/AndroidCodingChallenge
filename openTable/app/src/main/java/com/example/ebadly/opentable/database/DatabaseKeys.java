package com.example.ebadly.opentable.database;

public class DatabaseKeys {

    public static final String MOVIE_TABLE_NAME = "MOVIES_LIST";
    public static final String COLUMN_DISPLAY_TITLE = "DISPLAY_TITLE";
    public static final String COLUMN_MPAA_RATING = "MPAA_RATING";
    public static final String COLUMN_CRITICS_PICK = "CRITICS_PICK";
    public static final String COLUMN_BY_LINE = "BY_LINE";
    public static final String COLUMN_HEADLINE = "HEADLINE";
    public static final String COLUMN_SUMMARY_SHORT = "SUMMARY_SHORT";
    public static final String COLUMN_PUBLICATION_DATE = "PUBLICATION_DATE";
    public static final String COLUMN_OPENING_DATE = "OPENING_DATE";
    public static final String COLUMN_UPDATED_DATE = "UPDATED_DATE";
    public static final String MULTI_MEDIA_TABLE_NAME = "MULTI_MEDIA_LIST";
    public static final String COLUMN_MEDIA_TYPE = "MEDIA_TYPE";
    public static final String COLUMN_MEDIA_SRC = "MEDIA_SRC";
    public static final String COLUMN_MEDIA_WIDTH = "MEDIA_WIDTH";
    public static final String COLUMN_MEDIA_HEIGHT = "MEDIA_HEIGHT";

    public static final String SQL_CREATE_MOVIE_ENTRIES =
            "CREATE TABLE " + MOVIE_TABLE_NAME + " (" +
                    COLUMN_DISPLAY_TITLE + " TEXT," +
                    COLUMN_MPAA_RATING + " TEXT," +
                    COLUMN_CRITICS_PICK + " INT," +
                    COLUMN_BY_LINE + " TEXT," +
                    COLUMN_HEADLINE + " TEXT," +
                    COLUMN_SUMMARY_SHORT + " TEXT," +
                    COLUMN_PUBLICATION_DATE + " TEXT," +
                    COLUMN_UPDATED_DATE + " TEXT," +
                    COLUMN_MEDIA_TYPE + " TEXT," +
                    COLUMN_MEDIA_SRC + " TEXT," +
                    COLUMN_MEDIA_WIDTH + " TEXT," +
                    COLUMN_MEDIA_HEIGHT + " TEXT," +
                    COLUMN_OPENING_DATE + " TEXT)";

    public static final String SQL_DELETE_MOVIE_ENTRIES = "DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME;
}
