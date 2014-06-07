package com.example.mywolfpackguide;

import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class TwitterShare extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_share);
		try{
	        Intent intent = new Intent(Intent.ACTION_SEND);
	        intent.putExtra(Intent.EXTRA_TEXT, "this is a tweet");
	        intent.setType("text/plain");
	        final PackageManager pm = getPackageManager();
	        final List<?> activityList = pm.queryIntentActivities(intent, 0);
	        int len =  activityList.size();
	        for (int i = 0; i < len; i++) {
	            final ResolveInfo app = (ResolveInfo) activityList.get(i);
	            if ("com.twitter.android.PostActivity".equals(app.activityInfo.name)) {
	                final ActivityInfo activity=app.activityInfo;
	                final ComponentName name=new ComponentName(activity.applicationInfo.packageName, activity.name);
	                intent.addCategory(Intent.CATEGORY_LAUNCHER);
	                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	                intent.setComponent(name);
	                startActivity(intent);
	                break;
	            }
	        }
	  }
	    catch(final ActivityNotFoundException e) {
	        Log.i("twitter", "no twitter native",e );
	    }
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.twitter_share, menu);
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

	
	

}
