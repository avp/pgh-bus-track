package com.avp42.pghbustrack.view.route;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.data.PaacApi;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.models.vehicle.Vehicle;
import com.avp42.pghbustrack.util.Util;
import com.avp42.pghbustrack.view.MainActivity;
import com.avp42.pghbustrack.view.routes.VehicleArrayAdapter;
import java.io.IOException;
import java.util.List;

public class RouteDisplayFragment extends Fragment {
  private ListView vehicleListView;

  public static RouteDisplayFragment newInstance(Route route) {
    RouteDisplayFragment fragment = new RouteDisplayFragment();
    Bundle args = new Bundle();
    args.putSerializable("route", route);
    fragment.setArguments(args);
    return fragment;
  }

  public RouteDisplayFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_routedisplay, container, false);
    Route route = (Route) getArguments().getSerializable("route");
    getVehicleInfo(route);

    ActionBar actionBar = getActivity().getActionBar();
    if (actionBar != null) {
      ((MainActivity) getActivity()).getNavigationDrawerFragment().setDrawerIndicatorEnabled(false);
      setHasOptionsMenu(true);
    }

    int color = Color.parseColor(route.getColor());
    int textColor = Util.isColorDark(color) ? Color.WHITE : Color.BLACK;

    LinearLayout layoutHeading = (LinearLayout) view.findViewById(R.id.route_display_heading);
    layoutHeading.setBackgroundColor(color);

    TextView routeIdTextView = (TextView) view.findViewById(R.id.tv_route_id);
    routeIdTextView.setText(route.getId());
    routeIdTextView.setTextColor(textColor);

    TextView routeNameTextView = (TextView) view.findViewById(R.id.tv_route_name);
    routeNameTextView.setText(route.getName());
    routeNameTextView.setTextColor(textColor);

    vehicleListView = (ListView) view.findViewById(R.id.lv_vehicles);

    return view;
  }

  private void getVehicleInfo(final Route route) {
    new AsyncTask<Void, Void, List<Vehicle>>() {
      @Override
      protected List<Vehicle> doInBackground(Void... params) {
        try {
          return PaacApi.getInstance().getVehicles(route);
        } catch (IOException e) {
          return null;
        }
      }

      @Override
      protected void onPostExecute(List<Vehicle> vehicles) {
        super.onPostExecute(vehicles);
        if (vehicles == null) {
          Toast.makeText(getActivity(), "Unable to retrieve route information.", Toast.LENGTH_LONG).show();
          return;
        }
        VehicleArrayAdapter vehicleArrayAdapter = new VehicleArrayAdapter(getActivity(), vehicles);
        vehicleListView.setAdapter(vehicleArrayAdapter);
      }
    }.execute();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        getActivity().onBackPressed();
        return true;
    }
    return true;
  }
}
