package mk.com.findsellgive.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import mk.com.findsellgive.models.Rating;
import mk.com.findsellgive.models.User;
import mk.com.findsellgive.utills.LocationManager;
import mk.com.findsellgive.utills.PermissionsManager;
import mk.com.findsellgive.utills.SharedPreferencesManager;
import mk.com.findsellgive.utills.UtilitiesHelper;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseDatabase;//referenca do bazata
    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btRegister;
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;
    private Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseFirestore.getInstance();
        requestLocationPermission();
        initViews();
        initListeners();
    }


    private void initListeners() {
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = etFullName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Pasvordite ne se isti", Toast.LENGTH_SHORT).show();
                } else {
                    registerToFirebase(email, password, fullName);
                }
            }
        });
    }


    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btRegister = findViewById(R.id.bt_register);
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
                LocationManager.getInstance(RegisterActivity.this).getLastKnownLocation(new OnLocationFetchListener() {
                    @Override
                    public void onLocationFetch(Location location) {
                        userLocation = location;
                        SharedPreferencesManager.getInstance(RegisterActivity.this).setLocation(location.getLongitude(),location.getLatitude());
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

    private void registerToFirebase(String email, final String password, final String fullName) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FirebaseLogin", "createUserWithEmail:success");
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                GeoPoint location = new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude());
                                Rating rating = new Rating(0, 0, 0, 0, 0);
                                User user = new User(firebaseUser.getUid(), fullName, firebaseUser.getEmail(), password, "",
                                        rating, location);
                                firebaseDatabase.collection("users")
                                        .document(firebaseUser.getUid())
                                        .set(user.getMap(user)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Greska!", Toast.LENGTH_SHORT).show();
                                        }
                                        UtilitiesHelper.openNewPage(RegisterActivity.this, MainActivity.class);
                                        finish();
                                        etFullName.setText("");//se cisti tekstot od poleto
                                        etEmail.setText("");
                                        etPassword.setText("");
                                        etConfirmPassword.setText("");
                                    }
                                });

                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            //  Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
