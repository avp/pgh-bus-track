package com.avp42.pghbustrack.view.route;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.view.MainActivity;

public class RouteDisplayFragment extends Fragment {
  private Route route;

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
    route = (Route) getArguments().getSerializable("route");

    ActionBar actionBar = getActivity().getActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      ((MainActivity) getActivity()).getNavigationDrawerFragment().setDrawerIndicatorEnabled(false);
      setHasOptionsMenu(true);
    }

    TextView routeIdTextView = (TextView) view.findViewById(R.id.tv_route_id);
    routeIdTextView.setText(route.getId());

    return view;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Log.d("DEBUG", "HEFOIWJFPOEIFJEPOEFIJ;");
    switch (item.getItemId()) {
      case android.R.id.home:
        getActivity().onBackPressed();
        return true;
    }
    return true;
  }
}
