package com.avp42.pghbustrack.view.routes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.data.PaacApi;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.view.MainActivity;
import com.avp42.pghbustrack.view.route.RouteDisplayFragment;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteListFragment extends ListFragment {
  private static final String ARG_SECTION_NUMBER = "section_number";

  private List<Route> routes;

  public static RouteListFragment newInstance(int sectionNumber) {
    RouteListFragment fragment = new RouteListFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  public RouteListFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_routelist, container, false);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));

    ActionBar actionBar = getActivity().getActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(false);
      ((MainActivity) getActivity()).getNavigationDrawerFragment().setDrawerIndicatorEnabled(true);
    }

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

  @Override
  public void onListItemClick(ListView listView, View view, int position, long id) {
    super.onListItemClick(listView, view, position, id);
    Route route = (Route) listView.getAdapter().getItem(position);
    FragmentManager fragmentManager = getActivity().getFragmentManager();
    fragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
        .hide(this)
        .add(R.id.container, RouteDisplayFragment.newInstance(route))
        .addToBackStack(null)
        .commit();
  }

  private void setRoutes(List<Route> routes) {
    this.routes = routes;
    displayRoutes(routes);
  }

  private void displayRoutes(List<Route> routes) {
    RouteArrayAdapter arrayAdapter = new RouteArrayAdapter(getActivity(), routes);
    getListView().setAdapter(arrayAdapter);
  }
}
