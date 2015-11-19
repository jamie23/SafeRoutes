package com.android.jamie.saferoutes;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class RoutesActivityFragment extends Fragment {

    public RoutesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Create ArrayAdapter, populate it with data grabbed from google]
        //and use it to populate the listview.
        ArrayAdapter<String> mRoutesAdapter;

        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY",
                "Dummy data - Blah - DUMMY"
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


       SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
       String start = pref.getString(getString(R.string.start_point), null);
       String end = pref.getString(getString(R.string.end_point), null);

        return rootView;
    }
}
