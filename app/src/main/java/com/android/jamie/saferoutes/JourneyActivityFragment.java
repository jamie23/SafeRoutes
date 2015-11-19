package com.android.jamie.saferoutes;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Map;
import java.util.Set;


/**
 * A placeholder fragment containing a simple view.
 */
public class JourneyActivityFragment extends Fragment {

    protected GoogleApiClient mGoogleApiClient;

    private AutoCompleteTextView mAutocompleteView1;
    private AutoCompleteTextView mAutocompleteView2;

    private PlaceAutocompleteAdapter mAdapter;

    public static final String TAG = "JourneyActivityFragment";

    //This will need to be modified to be the users current lat long
    private static final LatLngBounds BOUNDS_LONDON = new LatLngBounds(
            new LatLng(51.440313, -0.280151), new LatLng(51.575363, 0.052185));


    public JourneyActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .build();
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
    }

    public void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journey_fragment, container, false);
        mAutocompleteView1 = (AutoCompleteTextView) rootView.findViewById(R.id.autocomplete_place1);

        mAutocompleteView2 = (AutoCompleteTextView) rootView.findViewById(R.id.autocomplete_place2);

        mAutocompleteView1.setOnItemClickListener(mAutocompleteClickListener);

        mAutocompleteView2.setOnItemClickListener(mAutocompleteClickListener);

        mAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, BOUNDS_LONDON,
                null);

        mAutocompleteView1.setAdapter(mAdapter);

        mAutocompleteView2.setAdapter(mAdapter);

        Button btnContinue = (Button) rootView.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(btnContinueListener);
        return rootView;
    }

    View.OnClickListener btnContinueListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d(TAG, mAutocompleteView1.getText().toString());

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sp.edit();

            editor.putString(getString(R.string.start_point), mAutocompleteView1.getText().toString());
            editor.putString(getString(R.string.end_point), mAutocompleteView2.getText().toString());
            editor.apply();

            Intent intent = new Intent(getActivity(), RoutesActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    };


    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);

            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };


}
