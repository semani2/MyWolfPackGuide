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
import android.os.Build;

public class Settings extends Activity {
	Button changePassword,updateInterests;
	SessionManager session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		session = new SessionManager(getApplicationContext());
		changePassword = (Button) findViewById(R.id.changePassword);
		updateInterests = (Button) findViewById(R.id.updateInterests);
		 changePassword.setOnClickListener(new View.OnClickListener() {
	            
	           @Override
	           public void onClick(View arg0) {
	               Intent i = new Intent(Settings.this, ChangePasswordActivity.class);
	               startActivity(i);
	               finish();
	           }
	       });
		 updateInterests.setOnClickListener(new View.OnClickListener() {
	            
	           @Override
	           public void onClick(View arg0) {
	               Intent i = new Intent(Settings.this, MyInterests.class);
	               startActivity(i);
	               finish();
	           }
	       });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if( id == R.id.action_logout)
		{
			session = new SessionManager(getApplicationContext());
			session.logoutUser();
			return true;
		}
		
		if( id == R.id.action_home)
		{
			Intent i = new Intent(Settings.this,HomeActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
