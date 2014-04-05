package com.parse.starter;

import java.io.IOException;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.parse.ParseAnalytics;

public class ParseStarterProjectActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	private EditText editText;
	private Location mLocation;
	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ParseAnalytics.trackAppOpened(getIntent());
		mLocationClient = new LocationClient(this, this, this);
		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create();
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		editText = (EditText) findViewById(R.id.buzzText);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					Location loc = getCurrentLoc();
					Message buzz = new Message(v.getText().toString(),loc);
					buzz.sendMessage();
					handled = true;
				}
				return handled;
			}

		});
	}
	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.connect();
		
	}

	@Override
	protected void onStop() {
		mLocationClient.disconnect();
		super.onStop();
	}
	private Location getCurrentLoc() {
		return mLocationClient.getLastLocation();
	}

	@Override
	public void onLocationChanged(Location location) {
		if(location!=null){
			mLocation = location;
			Toast.makeText(this, "Updated Location: " +
					Double.toString(location.getLatitude()) + "," +
					Double.toString(location.getLongitude())+ " and accuracy is "+location.getAccuracy(), Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(this, "Trying", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}
	public void onclickButton1(View v){
		Location loc = getCurrentLoc();
		Message buzz = new Message("trying",loc);
		buzz.sendMessage();
	}
}
