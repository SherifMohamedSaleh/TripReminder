package com.example.trip.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.trip.R;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener;

import static com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions.MODE_CARDS;


public class PlacesAutoCompleteFragment extends Fragment {
    private static final String TAG = "PlacesAutoCompleteFragm";
    private static final String YOUR_MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoidG9rYWFsaWFtaW4iLCJhIjoiY2pzODBzcjlrMTJ4azN5bnV6a3E2cTJiaSJ9.jWdMw48rKqQ9t-cd8J0KBA";
    private PlaceAutocompleteFragment autocompleteFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            autocompleteFragment = PlaceAutocompleteFragment.newInstance(YOUR_MAPBOX_ACCESS_TOKEN, PlaceOptions.builder().build(MODE_CARDS));
            final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, autocompleteFragment, TAG);
            transaction.commit();

        } else {
            autocompleteFragment = (PlaceAutocompleteFragment)
                    getActivity().getSupportFragmentManager().findFragmentByTag(TAG);
        }

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(CarmenFeature carmenFeature) {
                Toast.makeText(getActivity(),
                        carmenFeature.text(), Toast.LENGTH_LONG).show();
                //finish();
            }

            @Override
            public void onCancel() {
                //finish();
            }
        });
        return inflater.inflate(R.layout.fragment_places_auto_complete, container, false);
    }

}
