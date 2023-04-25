package com.open_activity.open_activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

/**
 * An activity to host AutocompleteSupportFragment from Places SDK.
 */
public class PlacePickerActivity extends FragmentActivity {

    public static Place getPlace(Intent data) {
        return data.getParcelableExtra("PLACE");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Field.LAT_LNG, Field.TYPES));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        setResult(RESULT_OK, new Intent().putExtra("PLACE", place));
                        finish();
                    }

                    @Override
                    public void onError(Status status) {
                        setResult(RESULT_CANCELED, new Intent().putExtra("STATUS", status));
                        finish();
                    }
                });
    }
}
