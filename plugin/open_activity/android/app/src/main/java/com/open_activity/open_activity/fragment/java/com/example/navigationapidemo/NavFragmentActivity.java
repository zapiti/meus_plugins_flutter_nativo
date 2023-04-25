package com.open_activity.open_activity.fragment.java.com.example.navigationapidemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CameraPerspective;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.libraries.navigation.ArrivalEvent;
import com.google.android.libraries.navigation.ListenableResultFuture;
import com.google.android.libraries.navigation.NavigationApi;
import com.google.android.libraries.navigation.Navigator;
import com.google.android.libraries.navigation.SimulationOptions;
import com.google.android.libraries.navigation.SupportNavigationFragment;
import com.google.android.libraries.navigation.Waypoint;
import com.google.android.libraries.places.api.model.Place;
import com.open_activity.open_activity.BuildConfig;
import com.open_activity.open_activity.PlacePickerActivity;
import com.open_activity.open_activity.R;
import com.open_activity.open_activity.base.java.com.example.navigationapidemo.NavForwardingManager;

/*
 * NOTE: BEFORE BUILDING THIS APPLICATION YOU MUST COPY THE GOOGLE NAVIGATION API
 * ANDROID LIBRARIES (AAR FILES) INTO THE libs/ DIRECTORY OF THIS APP AND UPDATE
 * THE API_KEY SPECIFIED IN YOUR ANDROID MANIFEST. THIS API KEY MUST BE ENABLED TO
 * ACCESS THE GOOGLE NAVIGATION API AND GOOGLE PLACES API.
 */

/**
 * This activity shows a simple Navigation API implementation using a Navigation fragment and using
 * the Google Places API for destination selection.
 */
public class NavFragmentActivity extends FragmentActivity {

    private static final int PLACE_PICKER_REQUEST = 1;

    GoogleMap mGoogleMap;
    Navigator mNavigator;
    SupportNavigationFragment mNavFragment;
    Fragment mNavInfoDisplayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_fragment);

        // Obtain a reference to the NavigationFragment
        mNavFragment =
                (SupportNavigationFragment)
                        getSupportFragmentManager().findFragmentById(R.id.navigation_fragment);

        // Ensure the screen stays on during nav.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initializeNavigationApi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up simulator if we are using it.
        if (mNavigator != null && BuildConfig.DEBUG) {
            mNavigator.getSimulator().unsetUserLocation();
        }
    }

    /**
     * Starts the Navigation API, saving a reference to the ready Navigator instance.
     */
    private void initializeNavigationApi() {
        NavigationApi.getNavigator(
                this,
                new NavigationApi.NavigatorListener() {
                    @Override
                    public void onNavigatorReady(Navigator navigator) {
                        // Keep a reference to the Navigator (used to configure and start nav)
                        mNavigator = navigator;
                        startFollowingLocationIfReady(CameraPerspective.TOP_DOWN_NORTH_UP);
                    }

                    @Override
                    public void onError(@NavigationApi.ErrorCode int errorCode) {
                        switch (errorCode) {
                            case NavigationApi.ErrorCode.NOT_AUTHORIZED:
                                // Note: If this message is displayed, you may need to check that
                                // your API_KEY is specified correctly in AndroidManifest.xml
                                // and is been enabled to access the Navigation API
                                showToast(
                                        "Error loading Navigation API: Your API key is "
                                                + "invalid or not authorized to use Navigation.");
                                break;
                            case NavigationApi.ErrorCode.TERMS_NOT_ACCEPTED:
                                showToast(
                                        "Error loading Navigation API: User did not "
                                                + "accept the Navigation Terms of Use.");
                                break;
                            default:
                                showToast("Error loading Navigation API: " + errorCode);
                        }
                    }
                });

        mNavFragment.getMapAsync(
                new OnMapReadyCallback() {
                    public void onMapReady(GoogleMap googleMap) {
                        mGoogleMap = googleMap;
                        startFollowingLocationIfReady(CameraPerspective.TOP_DOWN_NORTH_UP);
                    }
                });
    }

    /**
     * Starts following the device location in the map view once initialized.
     *
     * <p> Preconditions: {@link #mGoogleMap} and {@link #mNavigator} must be non-null; otherwise,
     * this is a no-op.
     *
     *  @param perspective {@link CameraPerspective} to use while following device location
     */
    @SuppressLint("MissingPermission") // TODO: requestPermissions(...) in here or earlier
    private void startFollowingLocationIfReady(@CameraPerspective int perspective) {
        if (mGoogleMap != null && mNavigator != null) {
            // Set the camera to follow the device location in the requested perspective
            mGoogleMap.followMyLocation(perspective);
        }
    }

    /**
     * Registers a number of example event listeners that show an on screen message when certain
     * navigation events occur (e.g. the driver's route changes or the destination is reached).
     */
    private void registerNavigationListeners() {
        mNavigator.addArrivalListener(
                new Navigator.ArrivalListener() {
                    @Override
                    public void onArrival(ArrivalEvent event) {
                        // Show an onscreen message
                        showToast("User has arrived at the destination!");

                        // Stop turn-by-turn guidance and return to TOP_DOWN perspective of the map
                        mNavigator.stopGuidance();
                        startFollowingLocationIfReady(CameraPerspective.TOP_DOWN_NORTH_UP);

                        // Stop simulating vehicle movement.
                        if (BuildConfig.DEBUG) {
                            mNavigator.getSimulator().unsetUserLocation();
                        }
                    }
                });

        mNavigator.addRouteChangedListener(
                new Navigator.RouteChangedListener() {
                    @Override
                    public void onRouteChanged() {
                        // Show an onscreen message when the route changes
                        showToast("onRouteChanged: the driver's route changed");
                    }
                });
    }

    /**
     * Requests directions from the user's current location to a specific place (provided by the
     * Google Places API).
     */
    private void navigateToPlace(Place place) {
        Waypoint waypoint;

        if (place.getTypes().contains(Place.Type.GEOCODE)) {
      // Show an example of setting a destination Lat-Lng
      // Note: Setting LatLng destinations can result in poor routing quality/ETA calculation.
      // Wherever possible you should use a Place ID to describe the destination accurately.
      waypoint =
          Waypoint.builder()
              .setLatLng(place.getLatLng().latitude, place.getLatLng().longitude)
              .build();
        } else {
            // Set a destination by using a Place ID (the recommended method)
            try {
              waypoint = Waypoint.builder().setPlaceIdString(place.getId()).build();
            } catch (Waypoint.UnsupportedPlaceIdException e) {
                showToast("Place ID was unsupported.");
                return;
            }
        }

        ListenableResultFuture<Navigator.RouteStatus> pendingRoute =
                mNavigator.setDestination(waypoint);

        // Set an action to perform when a route is determined to the destination
        pendingRoute.setOnResultListener(
                new ListenableResultFuture.OnResultListener<Navigator.RouteStatus>() {
                    @Override
                    public void onResult(Navigator.RouteStatus code) {
                        switch (code) {
                            case OK:
                                // Hide the toolbar to maximize the navigation UI
                                if (getActionBar() != null) {
                                    getActionBar().hide();
                                }

                                // Register some example listeners for navigation events
                                registerNavigationListeners();

                                // Enable voice audio guidance (through the device speaker)
                                mNavigator.setAudioGuidance(
                                        Navigator.AudioGuidance.VOICE_ALERTS_AND_GUIDANCE);

                                // Simulate vehicle progress along the route (for demo/debug builds)
                                if (BuildConfig.DEBUG) {
                                    mNavigator
                                            .getSimulator()
                                            .simulateLocationsAlongExistingRoute(
                                                    new SimulationOptions().speedMultiplier(5));
                                }

                                // Adjust camera to view route ahead
                                startFollowingLocationIfReady(CameraPerspective.TILTED);

                                // Start turn-by-turn guidance along the current route
                                mNavigator.startGuidance();
                                break;
                            case ROUTE_CANCELED:
                                // Return to top-down perspective
                                startFollowingLocationIfReady(CameraPerspective.TOP_DOWN_NORTH_UP);
                                showToast("Route guidance cancelled.");
                                break;
                            case NO_ROUTE_FOUND:
                            case NETWORK_ERROR:
                            default:
                                // TODO: Add logic to handle when a route could not be determined
                                showToast("Error starting guidance: " + String.valueOf(code));
                        }
                    }
                });
    }

    /**
     * Uses the Google Places API Place Picker to choose a destination to navigate to.
     *
     * <p>This method is referenced by the "Set Destination" item in menu_default.xml
     */
    public boolean showPlacePickerForDestination(MenuItem v) {
        try {
            startActivityForResult(new Intent(this, PlacePickerActivity.class),
                    PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            showToast(
                    "Could not display Place Picker. Check your API key has the Google"
                            + "Places API enabled.");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Only used in the withturnbyturn build flavor. Nothing will happen in the base flavor.
     *
     * Registers a service to receive navigation updates. The forwarded navigation information will
     * be displayed in a separate blue header above the map.
     */
    public boolean startNavForwarding(MenuItem v) {
        if (mNavInfoDisplayFragment == null) {
            mNavInfoDisplayFragment = NavForwardingManager.startNavForwarding(
                    mNavigator, this, getSupportFragmentManager());
        }
        return true;
    }

    /**
     * Only used in the withturnbyturn build flavor. Nothing will happen in the base flavor.
     *
     * Unregisters the service receiving navigation updates and hides the nav info display header.
     */
    public boolean stopNavForwarding(MenuItem v) {
        if (mNavInfoDisplayFragment != null) {
            NavForwardingManager.stopNavForwarding(
                    mNavigator,
                    this,
                    getSupportFragmentManager(),
                    mNavInfoDisplayFragment);
            mNavInfoDisplayFragment = null;
        }
      return true;
    }

    /**
     * If the Place Picker activity returns a destination, starts navigation to that place.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePickerActivity.getPlace(data);
                navigateToPlace(place);
            }
        }
    }

    private void showToast(String errorMessage) {
        Toast.makeText(NavFragmentActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_default, menu);
        return true;
    }
}
