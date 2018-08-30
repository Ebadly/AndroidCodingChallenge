package com.example.ebadly.opentable.database;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.ebadly.opentable.R;
import java.util.Calendar;

/**
 * Using this class to check for interval times
 * so there isn't a consistent/unessecary network calls
 * This class uses a time interval of 1 min
 * But also has a constructor to pass a custom interval
 */
public class SharedPrefManager {

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private long refreshTimeInterval;
    private String previousLookUpTimeKey;

    public SharedPrefManager(Context context) {
        this.preferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefs),
                Context.MODE_PRIVATE);

        this.editor = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefs),
                Context.MODE_PRIVATE).edit();
        refreshTimeInterval = 60*1000; // 1 min;
        previousLookUpTimeKey = context.getResources().getString(R.string.previous_lookup_time_key);
    }

    public SharedPrefManager(Context context, long refreshTimeInterval) {
        this.preferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefs),
                Context.MODE_PRIVATE);

        this.editor = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefs),
                Context.MODE_PRIVATE).edit();
        this.refreshTimeInterval = refreshTimeInterval;
        previousLookUpTimeKey = context.getResources().getString(R.string.previous_lookup_time_key);
    }

    public boolean isTimeToRefreshData(){
        long prevTime = preferences.getLong(previousLookUpTimeKey, 0);
        long nowTime = Calendar.getInstance().getTimeInMillis();
        return Math.abs(nowTime - prevTime) > refreshTimeInterval || prevTime == 0;
    }

    public void storeTimeCurrent(){
        editor.putLong(previousLookUpTimeKey, Calendar.getInstance().getTimeInMillis());
        editor.apply();
    }
}
