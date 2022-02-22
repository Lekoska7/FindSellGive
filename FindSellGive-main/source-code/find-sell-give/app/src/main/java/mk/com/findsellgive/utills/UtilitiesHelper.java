package mk.com.findsellgive.utills;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import mk.com.findsellgive.models.Rating;
import mk.com.findsellgive.models.User;


public class UtilitiesHelper {

    /**
     * funkcija sto otvora nova stranica so zadadenite parametri
     */
    public static void openNewPage(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void goToProfilePage(Context context, Class activity, String uid) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    public static void setRating(User user, MaterialRatingBar ratingBar) {
        if (user != null) {
            ratingBar.setRating(UtilitiesHelper.calculateRating(user.getRating()));
        }
    }

    private static float calculateRating(Rating rating) {
        float finalRating;
        finalRating = (
                (5 * rating.getRatingFive()) +
                        (4 * rating.getRatingFour()) +
                        (3 * rating.getRatingThree()) +
                        (2 * rating.getRatingTwo()) +
                        (1 * rating.getRatingOne())
        ) / rating.sum();
        return finalRating;
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, 1001);
            } else {
                activity.finish();
            }
            return false;
        }
        return true;
    }
}
