package com.avp42.pghbustrack.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.view.routes.RouteListFragment;
import com.avp42.pghbustrack.view.stops.StopListFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import static com.avp42.pghbustrack.util.Constants.Location.FASTEST_INTERVAL;
import static com.avp42.pghbustrack.util.Constants.Location.UPDATE_INTERVAL;


public class MainActivity extends Activity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks, LocationListener,
    GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
  /**
   * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
   */
  private NavigationDrawerFragment navigationDrawerFragment;

  /**
   * Used to store the last screen title. For use in {@link #restoreActionBar()}.
   */
  private CharSequence lastScreenTitle;

  private LocationClient locationClient;
  private LocationRequest locationRequest;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
    lastScreenTitle = getTitle();

    // Set up the drawer.
    navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

    // Set up location.
    locationClient = new LocationClient(this, this, this);
    locationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(UPDATE_INTERVAL)
        .setFastestInterval(FASTEST_INTERVAL);
  }

  @Override
  protected void onStart() {
    super.onStart();
    locationClient.connect();
  }

  @Override
  protected void onStop() {
    locationClient.disconnect();
    super.onStop();
  }

  @Override
  public void onNavigationDrawerItemSelected(int position) {
    // update the main content by replacing fragments
    FragmentManager fragmentManager = getFragmentManager();
    switch (position) {
      case 0:
        fragmentManager.beginTransaction()
            .replace(R.id.container, RouteListFragment.newInstance(position + 1))
            .commit();
        break;
      case 1:
        fragmentManager.beginTransaction().replace(R.id.container, StopListFragment.newInstance(position + 1)).commit();
        break;
    }
  }

  public void onSectionAttached(int number) {
    switch (number) {
      case 1:
        lastScreenTitle = getString(R.string.title_section1);
        break;
      case 2:
        lastScreenTitle = getString(R.string.title_section2);
        break;
    }
  }

  public void restoreActionBar() {
    ActionBar actionBar = getActionBar();
    if (actionBar != null) {
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
      actionBar.setDisplayShowTitleEnabled(true);
      actionBar.setTitle(lastScreenTitle);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (!navigationDrawerFragment.isDrawerOpen()) {
      // Only show items in the action bar relevant to this screen
      // if the drawer is not showing. Otherwise, let the drawer
      // decide what to show in the action bar.
      getMenuInflater().inflate(R.menu.main, menu);
      restoreActionBar();
      return true;
    }
    return super.onCreateOptionsMenu(menu);
  }

  public NavigationDrawerFragment getNavigationDrawerFragment() {
    return navigationDrawerFragment;
  }

  public Location getLocation() {
    return locationClient.getLastLocation();
  }

  @Override
  public void onConnected(Bundle bundle) {
    locationClient.requestLocationUpdates(locationRequest, this);
  }

  @Override
  public void onDisconnected() {}

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {}

  @Override
  public void onLocationChanged(Location location) {}
}
