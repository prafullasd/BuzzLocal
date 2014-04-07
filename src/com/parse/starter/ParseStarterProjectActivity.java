package com.parse.starter;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseStarterProjectActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	private EditText editText;
	private Location mLocation;
	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private ListView lv;
	private List<Map<String, String>> chatterList = new ArrayList<Map<String,String>>();
	private SimpleAdapter simpleAdpt;
	private SimpleAdapter currentAdpt;
	public Map<String, String> passedBuzz;
	public String passedBuzzid;
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
		lv = (ListView) findViewById(R.id.list);
		editText = (EditText) findViewById(R.id.buzzText);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					Location loc = getCurrentLoc();
					Message buzz = new Message(v.getText().toString(),loc, Calendar.getInstance().getTime());
					buzz.sendMessage();
					v.setText("");
					v.clearFocus();
					handled = true;
				}
				
				return handled;
			}

		});
		lv.setOnItemClickListener(new OnItemClickListener() {
		     @SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> parent, View view,
		         int position, long id) {

		         // When clicked, Open the Next Screen
		    	 Intent intent = new Intent(view.getContext(), DisplayBuzzActivity.class);
		    	 passedBuzz = (Map<String, String>) lv.getAdapter().getItem(position);
		    	 passedBuzzid = passedBuzz.get("buzzId");
		    	 intent.putExtra("com.parse.starter.passedBuzzid", passedBuzzid);
		         startActivityForResult(intent, position);
		         }
		     });
	}
	private HashMap<String, String> createBuzz(String key, String name) {
		    HashMap<String, String> buzz = new HashMap<String, String>();
		    buzz.put(key, name);
		    return buzz;
		}

	private void initList() {
		Location loc = getCurrentLoc();
		Message buzz = new Message("Hi. Welcome to BuzzLocal",loc, new Date());
		chatterList.add(createBuzz("buzz",buzz.getBuzz()));
		chatterList.add(createBuzz("buzz",buzz.getBuzz()));
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
	private ParseGeoPoint getParseCurrentLoc() {
		Location parsetemplocation = mLocationClient.getLastLocation();
		return new ParseGeoPoint(parsetemplocation.getLatitude(),parsetemplocation.getLongitude());
	}

	@Override
	public void onLocationChanged(Location location) {
		if(location!=null){
			mLocation = location;
//			Toast.makeText(this, "Updated Location: " +
//					Double.toString(location.getLatitude()) + "," +
//					Double.toString(location.getLongitude())+ " and accuracy is "+location.getAccuracy(), Toast.LENGTH_SHORT).show();
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
	public void onclickButton1(final View v) throws ParseException{
//		initList();
//		simpleAdpt = new SimpleAdapter(this, chatterList, R.layout.buzzrow, new String[] {"buzz"}, new int[] {android.R.id.text1});
//		lv.setAdapter(simpleAdpt);
		ParseGeoPoint userLocation = getParseCurrentLoc();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
		query.whereNear("buzzLoc", userLocation);
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DAY_OF_MONTH, -7);
		query.whereGreaterThan("buzzTime", rightNow.getTime());
		query.orderByDescending("buzzTime");
		query.orderByDescending("upvotes");
		query.setLimit(10);
		query.findInBackground(new FindCallback<ParseObject>() {
		     public void done(List<ParseObject> objects, ParseException e) {
		         if (e == null) {
		        	 Toast.makeText(v.getContext(), "Successful retrieval",
			     				Toast.LENGTH_SHORT).show();
		        	 currentAdpt = (SimpleAdapter) createAdapterfromQuery(objects);
		        	 lv.setAdapter(currentAdpt);
		         } else {
			        	Toast.makeText(v.getContext(), "Unsuccessful retrieval",
			     				Toast.LENGTH_SHORT).show();
		         }
		     }
		 });
	}
	private ListAdapter createAdapterfromQuery(List<ParseObject> objects) {
		List<Map<String, String>> queryList = new ArrayList<Map<String,String>>();
		for(ParseObject obj:objects){
			HashMap<String, String> buzz = new HashMap<String, String>();
		    buzz.put("buzz", obj.getString("buzz"));
		    DateFormat df = DateFormat.getDateTimeInstance();
		    buzz.put("buzztime", df.format(obj.getDate("buzzTime")));
		    buzz.put("buzzId", obj.getObjectId());
		    buzz.put("upvotes", ""+ obj.getInt("upvotes"));
			queryList.add(buzz);
		}
		return new SimpleAdapter(this, queryList, R.layout.buzzrow, new String[] {"buzz","buzztime","upvotes"}, new int[] {R.id.buzzText,R.id.date,R.id.upvotes});
	}
}
