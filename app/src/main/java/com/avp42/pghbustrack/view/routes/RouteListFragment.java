package com.avp42.pghbustrack.view.routes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.data.DataCache;
import com.avp42.pghbustrack.data.PaacApi;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.view.MainActivity;
import com.avp42.pghbustrack.view.route.RouteArrayAdapter;
import com.avp42.pghbustrack.view.route.RouteDisplayFragment;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RouteListFragment extends ListFragment {
  private static final String ARG_SECTION_NUMBER = "section_number";

  private RelativeLayout progressBar;

  public static RouteListFragment newInstance(int sectionNumber) {
    RouteListFragment fragment = new RouteListFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
    fragment.setArguments(args);
    return fragment;
  }

  public RouteListFragment() {}

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_routelist, container, false);

    progressBar = (RelativeLayout) view.findViewById(R.id.progress_routelist_loading);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    ActionBar actionBar = getActivity().getActionBar();
    if (actionBar != null) {
      ((MainActivity) getActivity()).getNavigationDrawerFragment().setDrawerIndicatorEnabled(true);
    }

    List<Route> cachedRoutes = DataCache.getRoutes(getActivity());

    if (cachedRoutes.isEmpty()) {
      progressBar.setVisibility(View.VISIBLE);
      getListView().setVisibility(View.GONE);
      getListView().getEmptyView().setVisibility(View.GONE);
    } else {
      setRoutes(cachedRoutes);
    }

    new AsyncTask<Void, Void, List<Route>>() {
      @Override
      protected void onPreExecute() {
        super.onPreExecute();
      }

      @Override
      protected List<Route> doInBackground(Void... params) {
        try {
          return PaacApi.getInstance().getRoutes();
        } catch (IOException e) {
          return Lists.newArrayList();
        }
      }

      @Override
      protected void onPostExecute(List<Route> routes) {
        setRoutes(routes);
      }
    }.execute();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
  }

  @Override
  public void onListItemClick(ListView listView, View view, int position, long id) {
    super.onListItemClick(listView, view, position, id);
    Route route = (Route) listView.getAdapter().getItem(position);
    FragmentManager fragmentManager = getActivity().getFragmentManager();
    fragmentManager.beginTransaction()
        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
        .remove(this)
        .add(R.id.container, RouteDisplayFragment.newInstance(route))
        .addToBackStack(null)
        .commit();
  }

  private void setRoutes(List<Route> routes) {
    Collections.sort(routes, new Comparator<Route>() {
      @Override
      public int compare(Route lhs, Route rhs) {
        return lhs.getId().compareTo(rhs.getId());
      }
    });

    DataCache.cacheRoutes(getActivity(), routes);

    ListView listView = getListView();
    listView.setVisibility(View.VISIBLE);
    listView.getEmptyView().setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);

    RouteArrayAdapter arrayAdapter = new RouteArrayAdapter(getActivity(), routes);
    listView.setAdapter(arrayAdapter);
  }
}
