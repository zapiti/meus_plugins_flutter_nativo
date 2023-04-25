package com.open_activity.open_activity.withturnbyturn.java.com.example.navigationapidemo;

import com.google.android.libraries.mapsplatform.turnbyturn.model.Maneuver;
import com.google.android.libraries.mapsplatform.turnbyturn.model.StepInfo;
import com.open_activity.open_activity.R;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

/*
 * NOTE: TO USE NAV INFO FORWARDING, PLEASE INCLUDE THE TURNBYTURN API JAR FILE IN THE libs/
 * DIRECTORY OF THIS APP, IN ADDITION TO THE GOOGLE NAVIGATION API AAR FILE.
 */

/**
 * Utility class that returns the drawable icon and string name of a given maneuver.
 */
public class ManeuverUtils {
    private static final Map<Integer, ManeuverInfo> mManeuverInfoMap =
        new HashMap<>();

    static {
        mManeuverInfoMap.put(Maneuver.UNKNOWN, new ManeuverInfo("UNKNOWN", R.drawable.ic_road));
        mManeuverInfoMap.put(Maneuver.DEPART, new ManeuverInfo("DEPART", R.drawable.ic_depart));
        mManeuverInfoMap.put(
                Maneuver.DESTINATION, new ManeuverInfo("DESTINATION", R.drawable.ic_destination));
        mManeuverInfoMap.put(
                Maneuver.DESTINATION_LEFT,
                new ManeuverInfo("DESTINATION_LEFT", R.drawable.ic_destination_left));
        mManeuverInfoMap.put(
                Maneuver.DESTINATION_RIGHT,
                new ManeuverInfo("DESTINATION_RIGHT", R.drawable.ic_destination_right));
        mManeuverInfoMap.put(Maneuver.STRAIGHT,
                new ManeuverInfo("STRAIGHT", R.drawable.ic_straight));
        mManeuverInfoMap.put(Maneuver.TURN_LEFT,
                new ManeuverInfo("TURN_LEFT", R.drawable.ic_turn_left));
        mManeuverInfoMap.put(Maneuver.TURN_RIGHT,
                new ManeuverInfo("TURN_RIGHT", R.drawable.ic_turn_right));
        mManeuverInfoMap.put(Maneuver.TURN_KEEP_LEFT,
                new ManeuverInfo("TURN_KEEP_LEFT", R.drawable.ic_fork_left));
        mManeuverInfoMap.put(
                Maneuver.TURN_KEEP_RIGHT,
                new ManeuverInfo("TURN_KEEP_RIGHT", R.drawable.ic_fork_right));
        mManeuverInfoMap.put(
                Maneuver.TURN_SLIGHT_LEFT,
                new ManeuverInfo("TURN_SLIGHT_LEFT", R.drawable.ic_turn_slight_left));
        mManeuverInfoMap.put(
                Maneuver.TURN_SLIGHT_RIGHT,
                new ManeuverInfo("TURN_SLIGHT_RIGHT", R.drawable.ic_turn_slight_right));
        mManeuverInfoMap.put(
                Maneuver.TURN_SHARP_LEFT,
                new ManeuverInfo("TURN_SHARP_LEFT", R.drawable.ic_turn_sharp_left));
        mManeuverInfoMap.put(
                Maneuver.TURN_SHARP_RIGHT,
                new ManeuverInfo("TURN_SHARP_RIGHT", R.drawable.ic_turn_sharp_right));
        mManeuverInfoMap.put(
                Maneuver.TURN_U_TURN_CLOCKWISE,
                new ManeuverInfo("TURN_U_TURN_CLOCKWISE", R.drawable.ic_turn_u_turn_clockwise));
        mManeuverInfoMap.put(
                Maneuver.TURN_U_TURN_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "TURN_U_TURN_COUNTERCLOCKWISE",
                        R.drawable.ic_turn_u_turn_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.MERGE_UNSPECIFIED,
                new ManeuverInfo("MERGE_UNSPECIFIED", R.drawable.ic_merge));
        mManeuverInfoMap.put(Maneuver.MERGE_LEFT,
                new ManeuverInfo("MERGE_LEFT", R.drawable.ic_merge_left));
        mManeuverInfoMap.put(Maneuver.MERGE_RIGHT,
                new ManeuverInfo("MERGE_RIGHT", R.drawable.ic_merge_right));
        mManeuverInfoMap.put(Maneuver.FORK_LEFT,
                new ManeuverInfo("FORK_LEFT", R.drawable.ic_fork_left));
        mManeuverInfoMap.put(Maneuver.FORK_RIGHT,
                new ManeuverInfo("FORK_RIGHT", R.drawable.ic_fork_right));
        mManeuverInfoMap.put(
                Maneuver.ON_RAMP_UNSPECIFIED,
                new ManeuverInfo("ON_RAMP_UNSPECIFIED", R.drawable.ic_straight));
        mManeuverInfoMap.put(Maneuver.ON_RAMP_LEFT,
                new ManeuverInfo("ON_RAMP_LEFT", R.drawable.ic_turn_left));
        mManeuverInfoMap.put(Maneuver.ON_RAMP_RIGHT,
                new ManeuverInfo("ON_RAMP_RIGHT", R.drawable.ic_turn_right));
        mManeuverInfoMap.put(
                Maneuver.ON_RAMP_KEEP_LEFT,
                new ManeuverInfo("ON_RAMP_KEEP_LEFT", R.drawable.ic_fork_left));
        mManeuverInfoMap.put(
                Maneuver.ON_RAMP_KEEP_RIGHT,
                new ManeuverInfo("ON_RAMP_KEEP_RIGHT", R.drawable.ic_fork_right));
        mManeuverInfoMap.put(
                Maneuver.ON_RAMP_SLIGHT_LEFT,
                new ManeuverInfo("ON_RAMP_SLIGHT_LEFT", R.drawable.ic_turn_slight_left));
        mManeuverInfoMap.put(
                Maneuver.ON_RAMP_SLIGHT_RIGHT,
                new ManeuverInfo("ON_RAMP_SLIGHT_RIGHT", R.drawable.ic_turn_slight_right));
        mManeuverInfoMap.put(
                Maneuver.ON_RAMP_SHARP_LEFT,
                new ManeuverInfo("ON_RAMP_SHARP_LEFT", R.drawable.ic_turn_sharp_left));
        mManeuverInfoMap.put(
                Maneuver.ON_RAMP_SHARP_RIGHT,
                new ManeuverInfo("ON_RAMP_SHARP_RIGHT", R.drawable.ic_turn_sharp_right));
        mManeuverInfoMap.put(
                Maneuver.ON_RAMP_U_TURN_CLOCKWISE,
                new ManeuverInfo("ON_RAMP_U_TURN_CLOCKWISE",
                        R.drawable.ic_turn_u_turn_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ON_RAMP_U_TURN_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ON_RAMP_U_TURN_COUNTERCLOCKWISE",
                        R.drawable.ic_turn_u_turn_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.OFF_RAMP_UNSPECIFIED,
                new ManeuverInfo("OFF_RAMP_UNSPECIFIED", R.drawable.ic_straight));
        mManeuverInfoMap.put(Maneuver.OFF_RAMP_LEFT,
                new ManeuverInfo("OFF_RAMP_LEFT", R.drawable.ic_merge_left));
        mManeuverInfoMap.put(
                Maneuver.OFF_RAMP_RIGHT,
                new ManeuverInfo("OFF_RAMP_RIGHT", R.drawable.ic_merge_right));
        mManeuverInfoMap.put(
                Maneuver.OFF_RAMP_KEEP_LEFT,
                new ManeuverInfo("OFF_RAMP_KEEP_LEFT", R.drawable.ic_fork_left));
        mManeuverInfoMap.put(
                Maneuver.OFF_RAMP_KEEP_RIGHT,
                new ManeuverInfo("OFF_RAMP_KEEP_RIGHT", R.drawable.ic_fork_right));
        mManeuverInfoMap.put(
                Maneuver.OFF_RAMP_SLIGHT_LEFT,
                new ManeuverInfo("OFF_RAMP_SLIGHT_LEFT", R.drawable.ic_turn_slight_left));
        mManeuverInfoMap.put(
                Maneuver.OFF_RAMP_SLIGHT_RIGHT,
                new ManeuverInfo("OFF_RAMP_SLIGHT_RIGHT", R.drawable.ic_turn_slight_right));
        mManeuverInfoMap.put(
                Maneuver.OFF_RAMP_SHARP_LEFT,
                new ManeuverInfo("OFF_RAMP_SHARP_LEFT", R.drawable.ic_turn_sharp_left));
        mManeuverInfoMap.put(
                Maneuver.OFF_RAMP_SHARP_RIGHT,
                new ManeuverInfo("OFF_RAMP_SHARP_RIGHT", R.drawable.ic_turn_sharp_right));
        mManeuverInfoMap.put(
                Maneuver.OFF_RAMP_U_TURN_CLOCKWISE,
                new ManeuverInfo("OFF_RAMP_U_TURN_CLOCKWISE",
                        R.drawable.ic_turn_u_turn_clockwise));
        mManeuverInfoMap.put(
                Maneuver.OFF_RAMP_U_TURN_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "OFF_RAMP_U_TURN_COUNTERCLOCKWISE",
                        R.drawable.ic_turn_u_turn_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_CLOCKWISE,
                new ManeuverInfo("ROUNDABOUT_CLOCKWISE", R.drawable.ic_roundabout_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_COUNTERCLOCKWISE",
                        R.drawable.ic_roundabout_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_STRAIGHT_CLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_STRAIGHT_CLOCKWISE",
                        R.drawable.ic_roundabout_straight_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_STRAIGHT_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_STRAIGHT_COUNTERCLOCKWISE",
                        R.drawable.ic_roundabout_straight_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_LEFT_CLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_LEFT_CLOCKWISE",
                        R.drawable.ic_roundabout_left_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_LEFT_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_LEFT_COUNTERCLOCKWISE",
                        R.drawable.ic_roundabout_left_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_RIGHT_CLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_RIGHT_CLOCKWISE",
                        R.drawable.ic_roundabout_right_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_RIGHT_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_RIGHT_COUNTERCLOCKWISE",
                        R.drawable.ic_roundabout_right_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_SLIGHT_LEFT_CLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_SLIGHT_LEFT_CLOCKWISE",
                        R.drawable.ic_roundabout_slight_left_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_SLIGHT_LEFT_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_SLIGHT_LEFT_COUNTERCLOCKWISE",
                        R.drawable.ic_roundabout_slight_left_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_SLIGHT_RIGHT_CLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_SLIGHT_RIGHT_CLOCKWISE",
                        R.drawable.ic_roundabout_slight_right_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_SLIGHT_RIGHT_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_SLIGHT_RIGHT_COUNTERCLOCKWISE",
                        R.drawable.ic_roundabout_slight_right_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_SHARP_LEFT_CLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_SHARP_LEFT_CLOCKWISE",
                        R.drawable.ic_roundabout_sharp_left_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_SHARP_LEFT_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_SHARP_LEFT_COUNTERCLOCKWISE",
                        R.drawable.ic_roundabout_sharp_left_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_SHARP_RIGHT_CLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_SHARP_RIGHT_CLOCKWISE",
                        R.drawable.ic_roundabout_sharp_right_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_SHARP_RIGHT_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_SHARP_RIGHT_COUNTERCLOCKWISE",
                        R.drawable.ic_roundabout_sharp_right_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_U_TURN_CLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_U_TURN_CLOCKWISE",
                        R.drawable.ic_roundabout_u_turn_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_U_TURN_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_U_TURN_COUNTERCLOCKWISE",
                        R.drawable.ic_roundabout_u_turn_counterclockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_EXIT_CLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_EXIT_CLOCKWISE",
                        R.drawable.ic_roundabout_exit_clockwise));
        mManeuverInfoMap.put(
                Maneuver.ROUNDABOUT_EXIT_COUNTERCLOCKWISE,
                new ManeuverInfo(
                        "ROUNDABOUT_EXIT_COUNTERCLOCKWISE",
                        R.drawable.ic_roundabout_exit_counterclockwise));
        mManeuverInfoMap.put(Maneuver.FERRY_BOAT,
                new ManeuverInfo("FERRY_BOAT", R.drawable.ic_ferry_boat));
        mManeuverInfoMap.put(Maneuver.FERRY_TRAIN,
                new ManeuverInfo("FERRY_TRAIN", R.drawable.ic_ferry_train));
        mManeuverInfoMap.put(Maneuver.NAME_CHANGE,
                new ManeuverInfo("NAME_CHANGE", R.drawable.ic_straight));
    }

    /**
     * Returns the string name of the step's maneuver.
     *
     * @param stepInfo the given step
     * @return the string name of the step's maneuver
     */
    @Nullable
    public static String getManeuverName(StepInfo stepInfo) {
        if (mManeuverInfoMap.containsKey(stepInfo.getManeuver())) {
            return mManeuverInfoMap.get(stepInfo.getManeuver()).name;
        }
        return null;
    }

    /**
     * Selects an appropriate icon for the maneuver of a given step.
     *
     * @param stepInfo the step
     * @return the resource id of the selected maneuver icon
     */
    public static int getManeuverIconResId(StepInfo stepInfo) {
        if (mManeuverInfoMap.containsKey(stepInfo.getManeuver())) {
            return mManeuverInfoMap.get(stepInfo.getManeuver()).iconResId;
        }
        return R.drawable.ic_straight;
    }

    /** Stores the maneuver's name and icon resource id. */
    private static class ManeuverInfo {
        String name;
        int iconResId;

        ManeuverInfo(String name, int iconResId) {
            this.name = name;
            this.iconResId = iconResId;
        }
    }
}
