package com.avp42.pghbustrack.view.route;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.data.DataCache;
import com.avp42.pghbustrack.data.PaacApi;
import com.avp42.pghbustrack.models.direction.Direction;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.models.stop.Stop;
import com.avp42.pghbustrack.util.Util;
import com.avp42.pghbustrack.view.FontLoader;
import com.avp42.pghbustrack.view.MainActivity;
import com.avp42.pghbustrack.view.stop.StopArrayAdapter;
import com.avp42.pghbustrack.view.stop.StopDisplayFragment;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import static com.avp42.pghbustrack.util.Constants.App.ROUTE_LIST_GRADIENT_FACTOR;

public class RouteDisplayFragment extends Fragment {
  private ListView stopListView;

  private RelativeLayout progressBar;

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
    Route route = (Route) getArguments().getSerializable("route");

    ActionBar actionBar = getActivity().getActionBar();
    if (actionBar != null) {
      ((MainActivity) getActivity()).getNavigationDrawerFragment().setDrawerIndicatorEnabled(false);
      setHasOptionsMenu(true);
    }

    LinearLayout layoutHeading = (LinearLayout) view.findViewById(R.id.route_display_heading);
    int darkColor = route.getDarkColor();
    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        new int[] {darkColor, Util.darken(darkColor, ROUTE_LIST_GRADIENT_FACTOR)});
    //noinspection deprecation
    layoutHeading.setBackgroundDrawable(gradientDrawable);

    TextView routeIdTextView = (TextView) view.findViewById(R.id.tv_route_id);
    routeIdTextView.setText(route.getId());
    routeIdTextView.setTypeface(FontLoader.getTypeface(getActivity(), FontLoader.ARMATA));

    TextView routeNameTextView = (TextView) view.findViewById(R.id.tv_route_name);
    routeNameTextView.setText(route.getName());
    routeNameTextView.setTypeface(FontLoader.getTypeface(getActivity(), FontLoader.ARMATA));

    progressBar = (RelativeLayout) view.findViewById(R.id.progress_routedisplay_loading);

    stopListView = (ListView) view.findViewById(R.id.lv_stops);
    stopListView.setEmptyView(progressBar);

    this.route = route;

    getStopInfo(route);
    return view;
  }

  private void getStopInfo(final Route route) {
    progressBar.setVisibility(View.VISIBLE);
    stopListView.setVisibility(View.GONE);

    setStops(DataCache.getStops(getActivity(), route));
    System.out.println(DataCache.getStops(getActivity(), route));

    new AsyncTask<Void, Void, Set<Stop>>() {
      @Override
      protected Set<Stop> doInBackground(Void... params) {
        try {
          List<Direction> directions = PaacApi.getInstance().getDirections(route);
          Set<Stop> stops = Sets.newHashSet();
          for (Direction direction : directions) {
            stops.addAll(PaacApi.getInstance().getStops(route, direction));
          }
          return stops;
        } catch (IOException e) {
          return Sets.newHashSet();
        }
      }

      @Override
      protected void onPostExecute(Set<Stop> stops) {
        super.onPostExecute(stops);
        setStops(stops);
      }
    }.execute();
  }

  private void setStops(final Collection<Stop> stopSet) {
    List<Stop> stops = Lists.newArrayList(stopSet);

    StopArrayAdapter stopArrayAdapter = new StopArrayAdapter(getActivity(), stops);
    stopListView.setAdapter(stopArrayAdapter);
    stopListView.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);

    Collections.sort(stops, new Comparator<Stop>() {
      @Override
      public int compare(Stop lhs, Stop rhs) {
        Location curLocation = ((MainActivity) getActivity()).getLocation();
        return Double.compare(lhs.distanceFrom(curLocation), rhs.distanceFrom(curLocation));
      }
    });

    DataCache.cacheStops(getActivity(), this.route, stops);

    final RouteDisplayFragment thisFragment = this;
    stopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Stop stop = (Stop) stopListView.getAdapter().getItem(position);
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            .remove(thisFragment)
            .add(R.id.container, StopDisplayFragment.newInstance(stop))
            .addToBackStack(null)
            .commit();
      }
    });
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
