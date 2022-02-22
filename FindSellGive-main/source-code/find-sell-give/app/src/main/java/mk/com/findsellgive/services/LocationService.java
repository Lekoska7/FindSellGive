package mk.com.findsellgive.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service  {
    private FusedLocationProviderClient mLocationClient;
    private static final long UPDATE_INTERVAL = 3*60*1000;//3 minutes
    private static final long FASTEST_INTERVAL = 60 * 1000;//1 minute
    private LocationCallback locationCallback;
    public static final String ACTION_LOCATION_CHANGE = "mk.com.findsellgive.LOCATION_CHANGE";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i("Location service","Location service started");
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult!=null){
                    if(locationResult.getLastLocation()!=null){
                        Location lastKnownLocation = locationResult.getLastLocation();
                        Log.i("New location",lastKnownLocation.getLatitude()+", "+lastKnownLocation.getLongitude());
                        Intent locationChange = new Intent(ACTION_LOCATION_CHANGE);
                        locationChange.putExtra("latitude",Double.toString(lastKnownLocation.getLatitude()));
                        locationChange.putExtra("longitude",Double.toString(lastKnownLocation.getLongitude()));
                        LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(locationChange);
                    }
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationClient.requestLocationUpdates(locationRequest,locationCallback,null);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
