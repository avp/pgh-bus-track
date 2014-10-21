package com.avp42.pghbustrack.view.stops;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.data.PaacApi;
import com.avp42.pghbustrack.models.direction.Direction;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.models.stop.Stop;
import com.avp42.pghbustrack.view.MainActivity;
import com.avp42.pghbustrack.view.stop.StopArrayAdapter;
import com.avp42.pghbustrack.view.stop.StopDisplayFragment;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class StopListFragment extends ListFragment {
  private static final String ARG_SECTION_NUMBER = "section_number";

  private RelativeLayout progressBar;

  private Activity activity;

  public static StopListFragment newInstance(int sectionNumber) {
    StopListFragment fragment = new StopListFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  public StopListFragment() {}

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_stoplist, container, false);

    progressBar = (RelativeLayout) view.findViewById(R.id.progress_stoplist_loading);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    ActionBar actionBar = this.activity.getActionBar();
    if (actionBar != null) {
      ((MainActivity) this.activity).getNavigationDrawerFragment().setDrawerIndicatorEnabled(true);
    }

    progressBar.setVisibility(View.VISIBLE);
    getListView().setVisibility(View.GONE);
    getListView().getEmptyView().setVisibility(View.GONE);

    new AsyncTask<Void, Void, Set<Stop>>() {
      @Override
      protected Set<Stop> doInBackground(Void... params) {
        try {
          PaacApi api = PaacApi.getInstance();
          Set<Stop> stops = Sets.newHashSet();
          for (Route route : api.getRoutes()) {
            for (Direction direction : api.getDirections(route)) {
              stops.addAll(api.getStops(route, direction));
            }
          }
          return stops;
        } catch (IOException e) {
          return Sets.newHashSet();
        }
      }

      @Override
      protected void onPostExecute(Set<Stop> stops) {
        setStops(stops);
      }
    }.execute();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    this.activity = activity;
    ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
  }

  @Override
  public void onListItemClick(ListView listView, View view, int position, long id) {
    super.onListItemClick(listView, view, position, id);
    Stop stop = (Stop) listView.getAdapter().getItem(position);
    FragmentManager fragmentManager = this.activity.getFragmentManager();
    fragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
        .remove(this)
        .add(R.id.container, StopDisplayFragment.newInstance(stop))
        .addToBackStack(null)
        .commit();
  }

  private void setStops(Set<Stop> stopSet) {
    List<Stop> stops = Lists.newArrayList(stopSet);

    Collections.sort(stops, new Comparator<Stop>() {
      @Override
      public int compare(Stop lhs, Stop rhs) {
        Location curLocation = ((MainActivity) activity).getLocation();
        return Double.compare(lhs.distanceFrom(curLocation), rhs.distanceFrom(curLocation));
      }
    });

    ListView listView = getListView();
    listView.setVisibility(View.VISIBLE);
    listView.getEmptyView().setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);

    StopArrayAdapter arrayAdapter = new StopArrayAdapter(this.activity, stops);
    listView.setAdapter(arrayAdapter);
  }
}
