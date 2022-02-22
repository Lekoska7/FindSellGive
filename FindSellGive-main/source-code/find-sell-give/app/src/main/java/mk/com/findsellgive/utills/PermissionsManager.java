package mk.com.findsellgive.utills;

import android.Manifest;
import android.app.Activity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import mk.com.findsellgive.listeners.PermissionDeniedListener;
import mk.com.findsellgive.listeners.PermissionGrantedListener;


public class PermissionsManager {
    private static PermissionsManager INSTANCE = null;
    private Activity activity;

    private PermissionsManager(Activity activity) {
        this.activity = activity;
    }

    public static synchronized PermissionsManager getInstance(Activity activity) {
        if (INSTANCE == null) {
           INSTANCE = new PermissionsManager(activity);
        }
        return INSTANCE;
    }

    public void requestLocationPermission(final PermissionGrantedListener listener, final PermissionDeniedListener permissionDeniedListener) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        listener.onPermissionGranted();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (!response.isPermanentlyDenied()) {
                            //TODO:request permission again
                        } else {
                            permissionDeniedListener.onPermissionDenied();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
}
