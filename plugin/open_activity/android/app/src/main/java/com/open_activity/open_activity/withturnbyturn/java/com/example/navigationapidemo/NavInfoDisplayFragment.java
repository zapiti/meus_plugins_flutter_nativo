package com.open_activity.open_activity.withturnbyturn.java.com.example.navigationapidemo;
import static java.lang.Math.max;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import com.google.android.libraries.mapsplatform.turnbyturn.model.DrivingSide;
import com.google.android.libraries.mapsplatform.turnbyturn.model.NavInfo;
import com.google.android.libraries.mapsplatform.turnbyturn.model.NavState;
import com.google.android.libraries.mapsplatform.turnbyturn.model.StepInfo;
import com.open_activity.open_activity.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*
 * NOTE: TO USE NAV INFO FORWARDING, PLEASE INCLUDE THE TURNBYTURN API JAR FILE IN THE libs/
 * DIRECTORY OF THIS APP, IN ADDITION TO THE GOOGLE NAVIGATION API AAR FILE.
 */

/**
 * Shows navigation information from the receiving service in a separate header fragment above
 * the base navigation fragment.
 */
public class NavInfoDisplayFragment extends Fragment {
    private static final String TAG = "NavInfoDisplay";

    /**
     * Conversion values for imperial measurement units. This sample app simply shows imperial
     * units. In your real app, you may want to use locale settings to determine whether to display
     * metric or imperial units.
     */
    private static final int MIN_MILES_TO_SHOW_INTEGER = 10;
    private static final int FEET_PER_MILE = 5280;
    private static final double FEET_PER_METER = 3.28;

    private final SimpleDateFormat mTimestampFormat =
            new SimpleDateFormat("HH:mm:ss.SSS z", Locale.US);
    private static final Map<Integer, String> mDrivingSideStrings = new HashMap<>();
    private static final int[] HEADER_TEXTVIEWS =
            new int[] {
                    R.id.tv_primary_text,
                    R.id.tv_time_to_step,
                    R.id.tv_distance_to_step,
                    R.id.tv_maneuver_type,
                    R.id.tv_full_instruction,
                    R.id.tv_timestamp,
                    R.id.tv_driving_side,
                    R.id.tv_roundabout_turn_number,
                    R.id.tv_destination_eta,
                    R.id.tv_remaining_distance
            };
    /** Set the header to blue for the current step. */
    private static final int CURRENT_STEP_COLOR = Color.parseColor("#4285F4");
    /** Set the header to blue-grey for step previews. */
    private static final int STEP_PREVIEW_COLOR = Color.parseColor("#617BA6");

    private View mDisplayHeader;
    private int mSelectedStepNumber = -1;
    private NavInfo mHeaderNavInfo;
    private boolean mShowingCurrentStep = true;

    static {
        mDrivingSideStrings.put(DrivingSide.NONE, "NONE");
        mDrivingSideStrings.put(DrivingSide.LEFT, "LEFT");
        mDrivingSideStrings.put(DrivingSide.RIGHT, "RIGHT");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_nav_info_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDisplayHeader = view;
        mDisplayHeader.findViewById(R.id.btn_next_step).setOnClickListener(v -> showNextStep());
        mDisplayHeader.findViewById(R.id.btn_prev_step).setOnClickListener(v -> showPrevStep());
        mDisplayHeader.findViewById(R.id.btn_current_step)
                .setOnClickListener(v -> showCurrentStep());
        showAwaitingNavigationText();
        // Observe live data for nav info updates.
        Observer<NavInfo> navInfoObserver = this::showNavInfo;
    NavInfoReceivingService.getNavInfoLiveData()
        .observe(this.getViewLifecycleOwner(), navInfoObserver);
    }

    private void showNavInfo(NavInfo navInfo) {
        if (navInfo == null) {
            return;
        }
        mHeaderNavInfo = navInfo;
        if (navInfo.getNavState() == NavState.REROUTING) {
            // Rerouting: Clear the header and indicate that we're rerouting.
            clearHeader();
            ((TextView) mDisplayHeader.findViewById(R.id.tv_primary_text)).setText("Rerouting...");
        } else if (navInfo.getNavState() == NavState.STOPPED) {
            // Stopped: Nav has stopped, so clear the header and indicate that we're awaiting
            // navigation.
            clearHeader();
            showAwaitingNavigationText();
        } else if (navInfo.getNavState() == NavState.ENROUTE
                && navInfo.getCurrentStep() != null
                && navInfo.getRemainingSteps() != null) {
            // Enroute:
            // Show the latest current step if
            //  1) The last shown step was the current step.
            //  2) This is the first step to be shown.
            //  3) If the route has changed since the last message.
            // Otherwise, continue to show whichever step is currently being shown, which may be
            // a step preview.
            if (navInfo.getRouteChanged()
                    || mSelectedStepNumber < 0
                    || mShowingCurrentStep
                    || !isStepNumberAvailable(navInfo, mSelectedStepNumber)) {
                mSelectedStepNumber = navInfo.getCurrentStep().getStepNumber();
            }
            showSelectedStep();
        } else {
            // Error.
            showToast("Received unknown NavInfo.");
        }
    }

    /**
     * Checks if a step number is part of the route. This includes the current step and remaining
     * steps.
     */
    private static boolean isStepNumberAvailable(NavInfo navInfo, int stepNumber) {
        if (navInfo == null || navInfo.getCurrentStep() == null) {
            return false;
        }
        int currentStepNumber = navInfo.getCurrentStep().getStepNumber();
        if (navInfo.getRemainingSteps().length < 1) {
            return stepNumber == currentStepNumber;
        }
        int lastAvailableStepNumber =
                navInfo.getRemainingSteps()[navInfo.getRemainingSteps().length - 1].getStepNumber();
        return currentStepNumber <= stepNumber && stepNumber <= lastAvailableStepNumber;
    }

    /** Shows the step selected by the user. This could be a current or remaining step. */
    private void showSelectedStep() {
        if (mHeaderNavInfo == null
                || mHeaderNavInfo.getCurrentStep() == null
                || mHeaderNavInfo.getRemainingSteps() == null) {
            return;
        }

        int currentStepNumber = mHeaderNavInfo.getCurrentStep().getStepNumber();
        StepInfo selectedStep = mHeaderNavInfo.getCurrentStep();
        if (mSelectedStepNumber != currentStepNumber) {
            // If the selected step is not the current step, then it must be a step preview.
            // Subtract the current step number from the selected step number to get the index
            // of the selected step in the array of remaining steps.
            selectedStep =
                    mHeaderNavInfo.getRemainingSteps()[mSelectedStepNumber - currentStepNumber - 1];
        }
        mShowingCurrentStep = selectedStep.getStepNumber() == currentStepNumber;

        // Show the full road name, maneuver icon, time and distance to step, and further details.
        ((TextView) mDisplayHeader.findViewById(R.id.tv_primary_text))
                .setText(selectedStep.getFullRoadName());

        setManeuverIcon(selectedStep);
        setTimeAndDistanceToSelectedStepTexts(selectedStep);
        setHeaderDetailTexts(selectedStep);

        // Enable or disable the current, previous, and next step buttons.
        setStepButtonsStates();
    }

    private void setTimeAndDistanceToSelectedStepTexts(StepInfo selectedStep) {
        // Get the estimated remaining time and distance to the current step.
        int distanceToStepMeters = mHeaderNavInfo.getDistanceToCurrentStepMeters();
        int timeToStepSeconds = mHeaderNavInfo.getTimeToCurrentStepSeconds();
        if (!isDisplayedStepCurrentStep()) {
            // If the displayed step is a future step preview rather than the current step, show
            // the entire time and distance for the step maneuver rather than the estimated
            // remaining time and distance to the current step.
            distanceToStepMeters = selectedStep.getDistanceFromPrevStepMeters();
            timeToStepSeconds = selectedStep.getTimeFromPrevStepSeconds();
        }

        // Show the time and distance to the selected step.
        ((TextView) mDisplayHeader.findViewById(R.id.tv_distance_to_step))
                .setText(getDistanceFormatted(distanceToStepMeters));
        String timeToStep =
                getTimeFormatted(timeToStepSeconds)
                        .append("to step #")
                        .append(mSelectedStepNumber)
                        .toString();
        ((TextView) mDisplayHeader.findViewById(R.id.tv_time_to_step)).setText(timeToStep);
    }

    /**
     * Returns whether the displayed step is a future step preview, rather than the current
     * upcoming step.
     */
    private boolean isDisplayedStepCurrentStep() {
        return mHeaderNavInfo.getCurrentStep() != null
                && mHeaderNavInfo.getCurrentStep().getStepNumber() == mSelectedStepNumber
                && mHeaderNavInfo.getDistanceToCurrentStepMeters() != null
                && mHeaderNavInfo.getTimeToCurrentStepSeconds() != null;
    }

    /**
     * Enable or disable the current, previous, and next step buttons based on whether those
     * steps are available.
     */
    private void setStepButtonsStates() {
        mDisplayHeader.findViewById(R.id.btn_next_step).setEnabled(canShowNextStep());
        mDisplayHeader
                .findViewById(R.id.btn_prev_step)
                .setEnabled(mSelectedStepNumber > mHeaderNavInfo.getCurrentStep().getStepNumber());
        mDisplayHeader.findViewById(R.id.btn_current_step).setEnabled(!mShowingCurrentStep);
        mDisplayHeader.setBackgroundColor(
                mShowingCurrentStep ? CURRENT_STEP_COLOR : STEP_PREVIEW_COLOR);
        mDisplayHeader.setVisibility(View.VISIBLE);
    }

    /** Displays the current step when the current step button is pressed. */
    private void showCurrentStep() {
        if (mHeaderNavInfo == null
                || mHeaderNavInfo.getCurrentStep() == null
                || mHeaderNavInfo.getRemainingSteps().length < 1) {
            return;
        }
        mSelectedStepNumber = mHeaderNavInfo.getCurrentStep().getStepNumber();
        showSelectedStep();
    }

    /** Returns whether the next step is available. */
    private boolean canShowNextStep() {
        StepInfo[] nextSteps = mHeaderNavInfo.getRemainingSteps();
        if (nextSteps.length < 1) {
            return false;
        }
        int lastAvailableStepNumber = nextSteps[nextSteps.length - 1].getStepNumber();
        return mSelectedStepNumber < lastAvailableStepNumber;
    }

    /** Displays the next step when the next step button is pressed. */
    private void showNextStep() {
        if (mHeaderNavInfo == null
                || mHeaderNavInfo.getRemainingSteps().length < 1
                || mSelectedStepNumber < 0
                || !canShowNextStep()) {
            return;
        }
        mSelectedStepNumber++;
        showSelectedStep();
    }

    /** Displays the previous step when the previous step button is pressed. */
    private void showPrevStep() {
        if (mHeaderNavInfo == null
                || mHeaderNavInfo.getRemainingSteps().length < 1
                || mSelectedStepNumber <= 0) {
            return;
        }
        mSelectedStepNumber--;
        showSelectedStep();
    }

    /** Shows the maneuver icon for the step. */
    private void setManeuverIcon(StepInfo stepInfo) {
        ((ImageView) mDisplayHeader.findViewById(R.id.iv_maneuver_icon))
                .setImageDrawable(
                        requireActivity()
                                .getResources()
                                .getDrawable(ManeuverUtils.getManeuverIconResId(stepInfo)));
    }

    /**
     * Returns the distance in the format of "mi" or "ft". Only shows ft if remaining distance is
     * less than 0.25 miles.
     *
     * @param distanceMeters the distance in meters.
     * @return the distance in the format of "mi" or "ft".
     */
    private String getDistanceFormatted(int distanceMeters) {
        // Distance can be negative so set the min distance to 0.
        // Only show the tenths place digit if the distance is less than 10 miles.
        // Only show feet if the distance is less than 0.25 miles.
        int remainingFeet = (int) max(0, distanceMeters * FEET_PER_METER);
        double remainingMiles = ((double) remainingFeet) / FEET_PER_MILE;
        String distance;
        if (remainingMiles >= MIN_MILES_TO_SHOW_INTEGER) {
            distance = (int) Math.round(remainingMiles) + " mi";
        } else if (remainingMiles >= 0.25) {
            distance = new DecimalFormat("0.0").format(remainingMiles) + " mi";
        } else {
            distance = remainingFeet + " ft";
        }
        return distance;
    }

    /**
     * Returns the time in the format of "hr min sec". Only shows hr if remaining minutes > 60. Only
     * shows min if remaining minutes % 60 != 0. Only shows sec if remaining minutes < 1.
     *
     * @param timeSeconds the time in seconds
     * @return the time in the format of "hr min sec".
     */
    private StringBuilder getTimeFormatted(int timeSeconds) {
        int remainingSeconds = max(0, timeSeconds);
        int remainingHours = remainingSeconds / 3600;
        int remainingMinutesRounded = (int) Math.round((remainingSeconds % 3600.0) / 60);
        StringBuilder timeBuilder = new StringBuilder();
        if (remainingHours > 0) {
            timeBuilder.append(remainingHours).append(" hr ");
        }
        if (remainingMinutesRounded > 0 && timeSeconds >= 60) {
            timeBuilder.append(remainingMinutesRounded).append(" min ");
        }
        if (remainingSeconds < 60) {
            timeBuilder.append(remainingSeconds).append(" sec ");
        }
        return timeBuilder;
    }

    /** Shows detailed navigation information. */
    private void setHeaderDetailTexts(StepInfo stepInfo) {
        ((TextView) mDisplayHeader.findViewById(R.id.tv_full_instruction))
                .setText(stepInfo.getFullInstructionText());
        ((TextView) mDisplayHeader.findViewById(R.id.tv_timestamp))
                .setText(mTimestampFormat.format(System.currentTimeMillis()));
        ((TextView) mDisplayHeader.findViewById(R.id.tv_roundabout_turn_number))
                .setText(Integer.toString(stepInfo.getRoundaboutTurnNumber()));
        ((TextView) mDisplayHeader.findViewById(R.id.tv_destination_eta))
                .setText(getTimeFormatted(mHeaderNavInfo.getTimeToDestinationSeconds()));
        ((TextView) mDisplayHeader.findViewById(R.id.tv_remaining_distance))
                .setText(getDistanceFormatted(mHeaderNavInfo.getDistanceToDestinationMeters()));

        setManeuverNameText(stepInfo);
        setDrivingSideText(stepInfo);
    }

    /** Shows the textual name of the maneuver. */
    private void setManeuverNameText(StepInfo stepInfo) {
        String maneuverName = ManeuverUtils.getManeuverName(stepInfo);
        if (maneuverName == null) {
            String error = "Error! Maneuver not found: " + stepInfo.getManeuver();
            showToast(error);
            Log.e(TAG, error);
        } else {
            ((TextView) mDisplayHeader.findViewById(R.id.tv_maneuver_type)).setText(maneuverName);
        }
    }

    /** Shows whether the step is in left-hand-traffic or right-hand-traffic. */
    private void setDrivingSideText(StepInfo stepInfo) {
        if (!mDrivingSideStrings.containsKey(stepInfo.getDrivingSide())) {
            String error = "Error! DrivingSide not found: " + stepInfo.getDrivingSide();
            showToast(error);
            Log.e(TAG, error);
        } else {
            ((TextView) mDisplayHeader.findViewById(R.id.tv_driving_side))
                    .setText(mDrivingSideStrings.get(stepInfo.getDrivingSide()));
        }
    }

    private void clearHeader() {
        ((ImageView) mDisplayHeader.findViewById(R.id.iv_maneuver_icon)).setImageDrawable(null);
        for (int tvId : HEADER_TEXTVIEWS) {
            ((TextView) mDisplayHeader.findViewById(tvId)).setText("");
        }
        mDisplayHeader.findViewById(R.id.btn_next_step).setEnabled(false);
        mDisplayHeader.findViewById(R.id.btn_prev_step).setEnabled(false);
        mDisplayHeader.findViewById(R.id.btn_current_step).setEnabled(false);
        mHeaderNavInfo = null;
        mShowingCurrentStep = true;
        mSelectedStepNumber = -1;
        mDisplayHeader.setBackgroundColor(CURRENT_STEP_COLOR);
    }

    private void showAwaitingNavigationText() {
        ((TextView) mDisplayHeader.findViewById(R.id.tv_primary_text))
                .setText("Awaiting navigation...");
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
