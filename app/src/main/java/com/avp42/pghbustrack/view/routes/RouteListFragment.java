package com.avp42.pghbustrack.view.routes;

import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.data.PaacApi;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.view.MainActivity;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteListFragment extends ListFragment {
  private static final String ARG_SECTION_NUMBER = "section_number";

  public static RouteListFragment newInstance(int sectionNumber) {
    RouteListFragment fragment = new RouteListFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  public RouteListFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_routelist, container, false);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));

    new AsyncTask<Void, Void, List<Route>>() {
      @Override
      protected List<Route> doInBackground(Void... params) {
        return PaacApi.getInstance().getRoutes();
      }

      @Override
      protected void onPostExecute(List<Route> routes) {
        setRoutes(routes);
      }
    }.execute();
  }

  private void setRoutes(List<Route> routes) {
    RouteArrayAdapter arrayAdapter = new RouteArrayAdapter(getActivity(), routes);
    getListView().setAdapter(arrayAdapter);
  }
}
