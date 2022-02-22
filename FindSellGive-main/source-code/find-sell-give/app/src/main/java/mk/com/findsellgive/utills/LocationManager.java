package mk.com.findsellgive.utills;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import mk.com.findsellgive.listeners.OnLocationFetchListener;


public class LocationManager {
    private static LocationManager INSTANCE = null;
    private FusedLocationProviderClient mLocationClient;

    private LocationManager(Context context) {
        mLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static synchronized LocationManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocationManager(context);
        }
        return INSTANCE;
    }

    public void getLastKnownLocation(final OnLocationFetchListener listener) {
        if (mLocationClient != null) {
            mLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        listener.onLocationFetch(location);
                    }
                }
            });
        }
    }
}
