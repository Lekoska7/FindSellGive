package mk.com.findsellgive.managers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mk.com.findsellgive.activities.LoginActivity;
import mk.com.findsellgive.activities.MainActivity;
import mk.com.findsellgive.utills.UtilitiesHelper;


public class FirebaseLoginManager {
    private Context context;
    private FirebaseLoginManager INSTANCE = null;
    private FirebaseAuth firebaseAuth;

    private FirebaseLoginManager(Context context) {
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public synchronized FirebaseLoginManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseLoginManager(context);
        }
        return INSTANCE;
    }
}
