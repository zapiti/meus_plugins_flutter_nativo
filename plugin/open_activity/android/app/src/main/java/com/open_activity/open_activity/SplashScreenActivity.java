package com.open_activity.open_activity;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.libraries.places.api.Places;

import java.util.concurrent.TimeUnit;

/**
 * The main activity showing a splash screen and requesting for location permission.
 */
public class SplashScreenActivity extends FragmentActivity {
    public static final String MAIN_ACTIVITY_INTENT_ACTION =
            "com.example.navigationapidemo.intent.action.MAIN";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;
    private static final long SPLASH_SCREEN_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(2);

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));
        setContentView(R.layout.activity_splash_screen);
        ImageView imageView = findViewById(R.id.splash_image);
        GlideApp.with(this)
                .load(
                        "http://services.google.com/fh/files/misc/google_maps_logo_480.png")
                .placeholder(R.drawable.google_maps_logo)
                .fitCenter()
                .into(imageView);

        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, permission.READ_CONTACTS)) {
                finish();
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
            }
        } else {
            new Handler().postDelayed(this::onLocationPermissionGranted,
                    SPLASH_SCREEN_DELAY_MILLIS);
        }
    }

    private void onLocationPermissionGranted() {
        Intent mainActivity = new Intent();
        mainActivity.setAction(MAIN_ACTIVITY_INTENT_ACTION);
        startActivity(mainActivity);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onLocationPermissionGranted();
                } else {
                    finish();
                }
            }
        }
    }
}
