package mk.com.findsellgive.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import mk.com.findsellgive.R;
import mk.com.findsellgive.listeners.OnLocationFetchListener;
import mk.com.findsellgive.listeners.PermissionDeniedListener;
import mk.com.findsellgive.listeners.PermissionGrantedListener;
import mk.com.findsellgive.utills.LocationManager;
import mk.com.findsellgive.utills.PermissionsManager;
import mk.com.findsellgive.utills.SharedPreferencesManager;
import mk.com.findsellgive.utills.UtilitiesHelper;

public class LoginActivity extends AppCompatActivity {
    private AppCompatEditText email;
    private AppCompatEditText password;
    private AppCompatButton signIn;
    private AppCompatButton register;
    private FirebaseAuth firebaseAuth;
    private String userEmail;
    private String userPassword;
    private Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //se vrsi inicijalizacija na komponentite vo ekranot
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        requestLocationPermission();
        initViews();
        initListeners();
    }

    private void requestLocationPermission() {
        boolean isPermissionEnabled = SharedPreferencesManager.getInstance(this).isLocationPermissionEnabled();
        if (!isPermissionEnabled) {
            PermissionsManager.getInstance(this).requestLocationPermission(new PermissionGrantedListener() {
                @Override
                public void onPermissionGranted() {
                    getLocation();
                }
            }, new PermissionDeniedListener() {
                @Override
                public void onPermissionDenied() {
                    //TODO: Open settings
                }
            });
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        PermissionsManager.getInstance(this).requestLocationPermission(new PermissionGrantedListener() {
            @Override
            public void onPermissionGranted() {
                LocationManager.getInstance(LoginActivity.this).getLastKnownLocation(new OnLocationFetchListener() {
                    @Override
                    public void onLocationFetch(Location location) {
                        userLocation = location;
                        SharedPreferencesManager.getInstance(LoginActivity.this).setLocation(location.getLongitude(), location.getLatitude());
                    }
                });
            }
        }, new PermissionDeniedListener() {
            @Override
            public void onPermissionDenied() {
                //TODO: Open settings
            }
        });
    }

    private void initListeners() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText() != null) {
                    userEmail = email.getText().toString();
                }
                if (password.getText() != null) {
                    userPassword = password.getText().toString();
                }
                signInToFirebase(userEmail, userPassword);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilitiesHelper.openNewPage(LoginActivity.this, RegisterActivity.class);
            }
        });
    }


    private void signInToFirebase(String userEmail, String userPassword) {
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //korisnikot e uspesno najaven
                    if (task.getResult() != null) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                                    .update("location", new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude()))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            UtilitiesHelper.openNewPage(LoginActivity.this, MainActivity.class);
                                        }
                                    });
                        }
                    }
                }
            }
        });
    }


    private void initViews() {
        email = findViewById(R.id.email_text);
        password = findViewById(R.id.password_text);
        signIn = findViewById(R.id.button_sign_in);
        register = findViewById(R.id.button_sign_up);
    }
}
