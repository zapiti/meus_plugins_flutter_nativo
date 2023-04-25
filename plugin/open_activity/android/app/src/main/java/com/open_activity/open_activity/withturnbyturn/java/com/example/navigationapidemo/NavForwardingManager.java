package com.open_activity.open_activity.withturnbyturn.java.com.example.navigationapidemo;

import android.content.Context;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.libraries.navigation.Navigator;
import com.open_activity.open_activity.R;

/*
 * NOTE: TO USE NAV INFO FORWARDING, PLEASE INCLUDE THE TURNBYTURN API JAR FILE IN THE libs/
 * DIRECTORY OF THIS APP, IN ADDITION TO THE GOOGLE NAVIGATION API AAR FILE.
 */

/**
 * Starts and stops the forwarding of turn-by-turn nav info from Nav SDK.
 */
public class NavForwardingManager {
    /**
     * Registers a service to receive navigation updates and creates a fragment to display the
     * received nav info.
     */
    public static Fragment startNavForwarding(
            Navigator navigator, Context context, FragmentManager fragmentManager) {
            boolean success = navigator.registerServiceForNavUpdates(
                    context.getPackageName(),
                    NavInfoReceivingService.class.getName(),
                    /* numNextStepsToPreview= */ Integer.MAX_VALUE); // Send all remaining steps.
            if (success) {
                Toast.makeText(context,
                        "Successfully registered service for nav updates",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,
                        "Failed to register service for nav updates",
                        Toast.LENGTH_SHORT).show();
            }
            Fragment navInfoDisplayFragment = new NavInfoDisplayFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.nav_info_frame, navInfoDisplayFragment)
                    .commit();
            return navInfoDisplayFragment;
    }

    /**
     * Unregisters the service receiving navigation updates and removes the nav info display
     * fragment.
     */
    public static void stopNavForwarding(
            Navigator navigator,
            Context context,
            FragmentManager fragmentManager,
            Fragment navInfoFragment) {
        // Remove the display header.
        fragmentManager
                .beginTransaction()
                .remove(navInfoFragment)
                .commit();
        // Unregister the nav info receiving service.
        boolean success = navigator.unregisterServiceForNavUpdates();
        if (success) {
            Toast.makeText(
                    context,
                    "Unregistered service for nav updates",
                    Toast.LENGTH_SHORT).show();
        } else {
            // This may happen if no service had been registered.
            Toast.makeText(
                    context,
                    "Failed to unregister service for nav updates",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
