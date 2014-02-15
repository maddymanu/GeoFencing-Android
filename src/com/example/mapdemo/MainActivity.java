package com.example.mapdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//import com.google.android.gms.location.LocationListener;

public class MainActivity extends Activity implements
		OnAddGeofencesResultListener, ConnectionCallbacks,
		OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {

	GoogleMap googleMap;
	PopupActivity textFragment;
	private LatLng currentLoc;
	private List<Geofence> mGeoList;
	private LocationManager locationManager;
	double clat, clong;

	LocationClient geoLocationClient;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;
	
	//wtf

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mGeoList = new ArrayList<Geofence>();

		geoLocationClient = new LocationClient(this, this, this);
		geoLocationClient.connect();

		MapFragment fm = (MapFragment) getFragmentManager().findFragmentById(
				R.id.map);
		googleMap = fm.getMap();

		googleMap.setMyLocationEnabled(true);

		mLocationRequest = LocationRequest.create();

		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(0);
		mLocationRequest.setFastestInterval(0);

		googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng position) {
				currentLoc = position;
				textFragment = new PopupActivity();
				textFragment.show(getFragmentManager(),
						"Opening Fragment to Get Text");

			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		geoLocationClient.connect();
	}

	// private void locate() {
	//
	// //geoLocationClient.connect();
	//
	// locationManager = (LocationManager)
	// getSystemService(Context.LOCATION_SERVICE);
	// LocationListener listener = new LocationListener() {
	//
	// @Override
	// public void onLocationChanged(Location location) {
	//
	// Geofence geofence1 = new Geofence.Builder()
	// .setRequestId("your target place")
	// .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
	// Geofence.GEOFENCE_TRANSITION_EXIT)
	// .setCircularRegion(0.0, 0.0, 2000.0f)
	// .setExpirationDuration(Geofence.NEVER_EXPIRE)
	// .build();
	// mGeoList.add(geofence1);
	//
	//
	// clat=location.getLatitude();
	// clong=location.getLongitude();
	//
	// Log.i("LOC Locate Method " + geoLocationClient.isConnected() ,
	// "YOLOSWAG");
	//
	// Log.i("LOC CURRENT LAT" , Location.convert(location.getLongitude(),
	// Location.FORMAT_DEGREES));
	//
	//
	//
	//
	// }
	//
	// @Override
	// public void onProviderDisabled(String provider) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onProviderEnabled(String provider) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onStatusChanged(String provider, int status,
	// Bundle extras) {
	// // TODO Auto-generated method stub
	//
	// }
	// };
	//
	//
	// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
	// 1,listener);
	// }

	private PendingIntent getPendingIntent() {
		Intent intent = new Intent(this, TransitionsIntentService.class);
		return PendingIntent.getService(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public void onUserSelectValue(String title, String extra) {
		Log.i("USERValue", title + extra);
		MarkerOptions marker = new MarkerOptions().position(
				new LatLng(currentLoc.latitude, currentLoc.longitude)).title(
				title);
		googleMap.addMarker(marker);

		// Starting geofencfing
		// Circle circle = googleMap.addCircle(new CircleOptions()
		// .center(new LatLng(currentLoc.latitude, currentLoc.longitude))
		// .radius(100)
		// .strokeColor(Color.RED)
		// .fillColor(Color.BLUE));

		Geofence geofence1 = new Geofence.Builder()
				.setRequestId(marker.getTitle())
				.setTransitionTypes(
						Geofence.GEOFENCE_TRANSITION_ENTER
								| Geofence.GEOFENCE_TRANSITION_EXIT)
				.setCircularRegion(currentLoc.latitude, currentLoc.longitude,
						2000.0f).setExpirationDuration(Geofence.NEVER_EXPIRE)
				.build();

		mGeoList.add(geofence1);

	}

	private void geofencing() {

		Geofence geofence1 = new Geofence.Builder()
				.setRequestId("WHY!")
				.setTransitionTypes(
						Geofence.GEOFENCE_TRANSITION_ENTER
								| Geofence.GEOFENCE_TRANSITION_EXIT)
				.setCircularRegion(clat, clong, 1.0f)
				.setExpirationDuration(0).build();

		mGeoList.add(geofence1);

		PendingIntent geoFencePendingIntent = PendingIntent.getService(this, 0,
				new Intent(this, TransitionsIntentService.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		geoLocationClient.addGeofences(mGeoList, geoFencePendingIntent, this);

	}

	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		// TODO Auto-generated method stub
		if (LocationStatusCodes.SUCCESS == statusCode) {
			/*
			 * Handle successful addition of geofences here. You can send out a
			 * broadcast intent or update the UI. geofences into the Intent's
			 * extended data.
			 */
		} else {
			// If adding the geofences failed
			/*
			 * Report errors here. You can log the error using Log.e() or update
			 * the UI.
			 */
		}
		// Turn off the in progress flag and disconnect the client
		// geoLocationClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnected(Bundle arg0) {
		boolean t = geoLocationClient.isConnected();
		Log.i("LOC onConnect    " + t, "THERE");
		geoLocationClient.requestLocationUpdates(mLocationRequest, this);
		Intent intent = new Intent(this, TransitionsIntentService.class);
		startService(intent);
		geofencing();

		mCurrentLocation = geoLocationClient.getLastLocation();

	}

	public void update() {
		geoLocationClient.addGeofences(mGeoList, getPendingIntent(), this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Log.i("LOC DISCONNECT    ", "OMG Y!");

	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

		mCurrentLocation = geoLocationClient.getLastLocation();

		clat = mCurrentLocation.getLatitude();
		clong = mCurrentLocation.getLongitude();
		update();


	}

	// @Override
	// public void onDestroy() {
	// super.onDestroy();
	// //geoLocationClient.disconnect();
	// }

}
