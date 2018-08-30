package com.example.ebadly.opentable;

import android.app.Application;
import com.facebook.stetho.Stetho;

//The only reason I have extended the application class
//is so that I can use Stetho for database debugging
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //I like using stetho for database viewing
        Stetho.initializeWithDefaults(this);
    }
}
