package com.parse.starter;

import java.text.DateFormat;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

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
	private TextView tv1;
	private TextView tv2;
	private boolean voted;
	private TextView tv3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_buzz);
		previousIntent = getIntent();
		voted = false;
		tv1 = (TextView) findViewById(R.id.spbuzzText);
		tv2 = (TextView) findViewById(R.id.spdate);
		tv3 = (TextView) findViewById(R.id.spupvotes);
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
				  public void done(ParseObject object, ParseException e) {
				    if (e == null) {
				    	thisBuzz = object;
				    	Toast.makeText(getBaseContext(),"Gotit", Toast.LENGTH_SHORT).show();
				    	tv1.setText(thisBuzz.getString("buzz"));
					    DateFormat df = DateFormat.getDateTimeInstance();
					    tv2.setText(df.format(thisBuzz.getDate("buzzTime")));
				    	tv3.setText(""+thisBuzz.getInt("upvotes"));
				    } else {
				    	Toast.makeText(getBaseContext(),"Oops", Toast.LENGTH_SHORT).show();
				    }
				  }
				});
	}
	
	public void doUpvote(View v){
		voted = true;
		Toast.makeText(getBaseContext(),"Voted!", Toast.LENGTH_SHORT).show();
		int temp = thisBuzz.getInt("upvotes");
		thisBuzz.put("upvotes",temp+1);
		try {
			thisBuzz.save();
		} catch (ParseException e) {
			Toast.makeText(getBaseContext(),"Error", Toast.LENGTH_SHORT).show();
		}
		
	}
}
