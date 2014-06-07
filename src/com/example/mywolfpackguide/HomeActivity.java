package com.example.mywolfpackguide;

import com.example.mywolfpackguide.Login.LoginTask;

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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.os.Build;

public class HomeActivity extends Activity {
	Button allEvents,interestedEvents,searchEvents,attendingEvents;
	SessionManager session;
	LinearLayout buttonLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		buttonLayout = (LinearLayout)findViewById(R.id.mLlayoutBottomButtons);
		allEvents = (Button) buttonLayout.findViewById(R.id.button1);
		interestedEvents = (Button)  buttonLayout.findViewById(R.id.button2);
		searchEvents = (Button)  buttonLayout.findViewById(R.id.button3);
		attendingEvents = (Button)  buttonLayout.findViewById(R.id.button4);
		session = new SessionManager(getApplicationContext());
		
		session.checkLogin();
		allEvents.setOnClickListener(new View.OnClickListener() {
            
	           @Override
	           public void onClick(View arg0) {
	              Intent i = new Intent(HomeActivity.this,AllEventsNew.class);
	               startActivity(i); 
	           }
	       });
		
		interestedEvents.setOnClickListener(new View.OnClickListener() {
            
	           @Override
	           public void onClick(View arg0) {
	              Intent i = new Intent(HomeActivity.this,InterestEvents.class);
	               startActivity(i); 
	           }
	       });
		
		searchEvents.setOnClickListener(new View.OnClickListener() {
            
	           @Override
	           public void onClick(View arg0) {
	              Intent i = new Intent(HomeActivity.this,SearchEvents.class);
	               startActivity(i); 
	           }
	       });
		
		attendingEvents.setOnClickListener(new View.OnClickListener() {
            
	           @Override
	           public void onClick(View arg0) {
	              Intent i = new Intent(HomeActivity.this,UserEvents.class);
	               startActivity(i); 
	           }
	       });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(HomeActivity.this,Settings.class);
			startActivity(i);
			return true;
		}
		if( id == R.id.action_logout)
		{
			session = new SessionManager(getApplicationContext());
			session.logoutUser();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
