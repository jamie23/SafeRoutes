package com.android.jamie.saferoutes;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class RoutesActivityFragment extends Fragment {

    //Populated with data grabbed from google directions API
    //used it to populate the listview for user selection
    ArrayAdapter<String> mRoutesAdapter;

    public RoutesActivityFragment() {
    }

    public void onStart(){
        super.onStart();
        FetchRoutes myFetchRoutes = new FetchRoutes();
        myFetchRoutes.execute("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
        };

        List<String> dummyList = new ArrayList<String>(Arrays.asList(data));

        mRoutesAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_route, // The name of the layout ID.
                        R.id.list_item_route_textview, // The ID of the textview to populate.
                        dummyList);

        View rootView = inflater.inflate(R.layout.fragment_routes, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_routes);
        listView.setAdapter(mRoutesAdapter);

        return rootView;
    }

    public class FetchRoutes extends AsyncTask<String, Void, JSONObject[]>{

        private final String LOG_TAG = FetchRoutes.class.getSimpleName();

        /*  Example request
            https://maps.googleapis.com/maps/api/directions/json?origin=Brooklyn&destination=Queens&departure_time=1343641500&mode=transit
         */
        @Override
        protected JSONObject[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String start = pref.getString(getString(R.string.start_point), null);
            String end = pref.getString(getString(R.string.end_point), null);

            try {
                String startLocation = start;
                String destination = end;
                String alternatives = "true";
                String mode = "walking";

                final String ORIGIN= "origin=";
                final String DESTINATION = "&destination=";
                final String ALTERNATIVES = "&alternatives=";
                final String MODE = "&mode=";
                //final String API_KEY = "key";
                final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";

                StringBuilder directionsURL = new StringBuilder(300);

                directionsURL.append(BASE_URL);
                directionsURL.append(ORIGIN);
                directionsURL.append(startLocation);
                directionsURL.append(DESTINATION);
                directionsURL.append(destination);
                directionsURL.append(MODE);
                directionsURL.append(mode);
                directionsURL.append(ALTERNATIVES);
                directionsURL.append(alternatives);

                URL url = new URL(directionsURL.toString().replace(" ",""));

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
          }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // print out the completed buffer for debugging.
                    buffer.append(line + "\n");
                }
            }catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        //
        @Override
        protected void onPostExecute(JSONObject[] routeData) {
            mRoutesAdapter.clear();

           //Must do processing here to get string to populate mRoutesAdapter


            //Routes adapter must take a string
            mRoutesAdapter.addAll();
        }
    }
}
