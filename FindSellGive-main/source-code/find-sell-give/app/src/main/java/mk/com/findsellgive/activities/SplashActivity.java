package mk.com.findsellgive.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mk.com.findsellgive.R;
import mk.com.findsellgive.utills.UtilitiesHelper;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();//korisnik od baza
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseUser!=null){

                    UtilitiesHelper.openNewPage(SplashActivity.this, MainActivity.class);
                    finish();
                }else{

                    UtilitiesHelper.openNewPage(SplashActivity.this, LoginActivity.class);
                    finish();
                }
            }
        },2000);
    }
}
