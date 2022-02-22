package mk.com.findsellgive.utills;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesManager {
    private static SharedPreferencesManager INSTANCE = null;
    private SharedPreferences preferences;
    private Context context;
    private final String PREFS = "shared_prefs";
    private final String LOCATION = "location_permission";
    private final String LONGITUDE = "longitude";
    private final String LATITUDE = "latitude";

    private SharedPreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferencesManager(context);
        }
        return INSTANCE;
    }

    public void setLocationPermissionEnabled(boolean isEnabled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOCATION, isEnabled).apply();
    }

    public boolean isLocationPermissionEnabled() {
        return preferences.getBoolean(LOCATION, false);
    }

    public void setLocation(double lon, double lat) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LATITUDE, Double.toString(lat));
        editor.putString(LONGITUDE, Double.toString(lon)).apply();
    }
    public double getLatitude(){
        return Double.parseDouble(preferences.getString(LATITUDE,null));
    }
    public double getLongitude(){
        return Double.parseDouble(preferences.getString(LONGITUDE,null));
    }
}
