package com.open_activity.open_activity.base.java.com.example.navigationapidemo;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.libraries.navigation.Navigator;

/**
 * Do nothing for the base flavor, which does not use the nav forwarding API.
 */
public class NavForwardingManager {
    public static Fragment startNavForwarding(
            Navigator navigator, Context context, FragmentManager fragmentManager) {
        return null;
    }

    public static void stopNavForwarding(
          Navigator navigator,
          Context context,
          FragmentManager fragmentManager,
          Fragment navInfoFragment) {}
}
