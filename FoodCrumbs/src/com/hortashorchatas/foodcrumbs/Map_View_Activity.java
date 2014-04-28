package com.hortashorchatas.foodcrumbs;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.os.Build;
import android.provider.Settings;

public class Map_View_Activity extends Activity implements SearchView.OnQueryTextListener {
	private GoogleMap gMaps;
	private MarkerOptions markerOptions;
	private LatLng myCurrCoordinates;
	private Location myLocation;
	private LocationManager locationServices;
	private String provider;
	private MapFragment mMapFragment;
	
	/**
	 * This method creates the view at the onset of the Activity. One thing to check here.
	 * Check if the Alert Dialog gets built, and if all the correct settings are changed,
	 * if the dialog will not popup anymore afterwards.
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map__view);
		
		locationServices = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = locationServices.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// check if dialog pops up when Location services is not available and then 
		// the dialog does not appear again if location services is available
		if (!enabled) {
			AlertDialog.Builder enableLocationServices = new AlertDialog.Builder(this);
			enableLocationServices.setTitle("Location Services Not Active.");
			enableLocationServices.setMessage("Please enable Location Services and GPS.");
			enableLocationServices.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				    startActivity(intent);
				  }
				});
			enableLocationServices.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					finish();
				}
			});
			enableLocationServices.show();
		}
		
		mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
				
        gMaps = mMapFragment.getMap();
        
		gMaps.setMyLocationEnabled(true);
		gMaps.getUiSettings().setMyLocationButtonEnabled(true);
		
        zoomToCurrLocation();
	}

	/**
	 * This method creates the option menu at the top of the android. The search icon is found in the menu
	 * and we give it a SearchView. Therefore, if the search icon is clicked, then a SearchView should replace
	 * everything else on the menu with the query hint "Enter a Location". 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map__view_, menu);
		
		MenuItem searchItem = menu.findItem(R.id.action_search);
		final SearchView searchLocation = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchLocation.setQueryHint("Enter a Location");
		searchLocation.setIconifiedByDefault(true);
		searchLocation.setOnQueryTextListener(this);

		return true;
	}

	/**
	 * This method controls the selection of the items on the menu bar at the very top of the android. 
	 * Check to see if when the directions icon is clicked it starts the Directions Activity. If it 
	 * does not start the directions activity something is wrong.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_directions) {
			Intent i = new Intent(getApplicationContext(), Directions_Activity.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Currently, find location should log the pair "Hehehe" with the query that you put into
	 * the search bar. Eventually it will do more things...
	 * @param query
	 */
	private void findLocation(String query) {
		Log.v("Hehehe", query);
	}
	
	/**
	 * This should zoom to your current location on the map. If it does not go to your 
	 * current location on the map, something is wrong.
	 */
	private void zoomToCurrLocation() {		
		Criteria criteria = new Criteria();
		provider = locationServices.getBestProvider(criteria, false);
		
		myLocation = locationServices.getLastKnownLocation(provider);
        if (myLocation != null)
        {
            gMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 16));
        }
	}

	@Override
	public boolean onQueryTextChange(String text) {
		return false;
	}

	/**
	 * Handles the submission of the SearchView on the menu bar. It takes the string in the 
	 * SearchView and calls the method findLocation.
	 */
	@Override
	public boolean onQueryTextSubmit(String text) {
		// TODO Auto-generated method stub
		findLocation(text);
		return false;
	}

}
