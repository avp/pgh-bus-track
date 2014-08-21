package com.avp42.pghbustrack.view.route;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
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
import com.avp42.pghbustrack.models.direction.Direction;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.models.stop.Stop;
import com.avp42.pghbustrack.util.Util;
import com.avp42.pghbustrack.view.MainActivity;
import java.io.IOException;
import java.util.List;
import static com.avp42.pghbustrack.util.Constants.App.ROUTE_LIST_GRADIENT_FACTOR;

public class RouteDisplayFragment extends Fragment {
  private ListView stopListView;

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

    LinearLayout layoutHeading = (LinearLayout) view.findViewById(R.id.route_display_heading);
    int darkColor = route.getDarkColor();
    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        new int[]{darkColor, Util.darken(darkColor, ROUTE_LIST_GRADIENT_FACTOR)});
    layoutHeading.setBackgroundDrawable(gradientDrawable);

    TextView routeIdTextView = (TextView) view.findViewById(R.id.tv_route_id);
    routeIdTextView.setText(route.getId());

    TextView routeNameTextView = (TextView) view.findViewById(R.id.tv_route_name);
    routeNameTextView.setText(route.getName());

    stopListView = (ListView) view.findViewById(R.id.lv_stops);

    return view;
  }

  private void getVehicleInfo(final Route route) {
    new AsyncTask<Void, Void, List<Stop>>() {
      @Override
      protected List<Stop> doInBackground(Void... params) {
        try {
          List<Direction> directions = PaacApi.getInstance().getDirections(route);
          return PaacApi.getInstance().getStops(route, directions.get(0));
        } catch (IOException e) {
          return null;
        }
      }

      @Override
      protected void onPostExecute(List<Stop> stops) {
        super.onPostExecute(stops);
        if (stops == null) {
          Toast.makeText(getActivity(), "Unable to retrieve route information.", Toast.LENGTH_LONG).show();
          return;
        }
        StopArrayAdapter stopArrayAdapter = new StopArrayAdapter(getActivity(), stops);
        stopListView.setAdapter(stopArrayAdapter);
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
