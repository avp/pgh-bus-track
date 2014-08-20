package com.avp42.pghbustrack.view.route;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.data.PaacApi;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.models.vehicle.Vehicle;
import com.avp42.pghbustrack.view.MainActivity;
import com.avp42.pghbustrack.view.routes.VehicleArrayAdapter;
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
      actionBar.setDisplayHomeAsUpEnabled(true);
      ((MainActivity) getActivity()).getNavigationDrawerFragment().setDrawerIndicatorEnabled(false);
      setHasOptionsMenu(true);
    }

    TextView routeIdTextView = (TextView) view.findViewById(R.id.tv_route_id);
    routeIdTextView.setText(route.getId());

    TextView routeNameTextView = (TextView) view.findViewById(R.id.tv_route_name);
    routeNameTextView.setText(route.getName());

    vehicleListView = (ListView) view.findViewById(R.id.lv_vehicles);

    return view;
  }

  private void getVehicleInfo(final Route route) {
    new AsyncTask<Void, Void, List<Vehicle>>() {
      @Override
      protected List<Vehicle> doInBackground(Void... params) {
        return PaacApi.getInstance().getVehicles(route);
      }

      @Override
      protected void onPostExecute(List<Vehicle> vehicles) {
        super.onPostExecute(vehicles);
        Log.d("DEBUG", vehicles.toString());
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
