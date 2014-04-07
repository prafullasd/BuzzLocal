package com.parse.starter;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class DisplayBuzzActivity extends Activity {
	Intent previousIntent;
	String passedBuzzid;
	ParseObject thisBuzz;
	private TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_buzz);
		previousIntent = getIntent();
		
		tv = (TextView) findViewById(R.id.textView1);	
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_buzz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_display_buzz,
					container, false);
			return rootView;
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		passedBuzzid = (String) previousIntent.getExtras().getString("com.parse.starter.passedBuzzid");
		Toast.makeText(this,passedBuzzid , Toast.LENGTH_SHORT).show();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
		query.getInBackground(passedBuzzid, new GetCallback<ParseObject>() {
		@Override
		public void done(ParseObject object, ParseException e) {
			if (e == null) {
			      thisBuzz = object;
			      tv.setText(thisBuzz.getString("buzz"));
			    } else {
			    	Toast.makeText(getBaseContext(),"Oops", Toast.LENGTH_SHORT).show();
			    }
			
		}
		});

	}
}
