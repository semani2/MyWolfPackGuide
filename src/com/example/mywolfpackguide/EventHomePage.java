package com.example.mywolfpackguide;

import java.net.URI;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;

public class EventHomePage extends Activity {

	String eventID = null,imageURL,rsvpStatus = null,Username = null;
	TextView eventName,desc,location,date,time,price,temperature,weather,categoryName;
	String EventName,Desc,Location,Date,Time,Price,Temperature,Weather,CategoryName;
	ImageView weatherImage;
	Button attendees,attend,sync,share;
	SessionManager session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_event_home_page);
		 session = new SessionManager(getApplicationContext());
		 Username = session.getUserDetails();
		 attendees = (Button)findViewById(R.id.changePassword);
		attend = (Button)findViewById(R.id.button2);
		sync = (Button)findViewById(R.id.button3);
		//share = (Button)findViewById(R.id.button4);
		
		
		//Get Intent details , for eventID
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    eventID = extras.getString("eventID");
		    Log.d("Event Id in home page", eventID);
		}
		MyTask m = new MyTask();
		m.execute();
		
		attend.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) { 
				
				EventRSVP ersvp = new EventRSVP();
				ersvp.execute();
				
			}
			});
		attendees.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) { 
				
				 Intent i = new Intent(getApplicationContext(), EventAttendees.class);
                 // sending data to new activity
                 i.putExtra("eventID",eventID);
                 startActivity(i);
				
			}
			});
		
		/* share.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) { 
				
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
			}); */
		
		sync.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) { 
				String[] parts = Date.split("-");
				Intent calIntent = new Intent(Intent.ACTION_INSERT); 
				calIntent.setType("vnd.android.cursor.item/event");    
				calIntent.putExtra(Events.TITLE, EventName); 
				calIntent.putExtra(Events.EVENT_LOCATION, Location); 
				calIntent.putExtra(Events.DESCRIPTION, Desc); 
				Log.d("year for event", parts[0]);
				Log.d("Month for event", parts[1]);
				Log.d("Date for event", parts[2]);
				GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])-1, Integer.parseInt(parts[2]));
				calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true); 
				calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, 
				     calDate.getTimeInMillis()); 
				calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, 
				     calDate.getTimeInMillis()); 
				 
				startActivity(calIntent);
			}
			});
	}

	
	
	//Async Task
	
	public class MyTask extends AsyncTask<String,Void,Void>
	{
		RestServiceUrl s = new RestServiceUrl();
		@Override
		protected Void doInBackground(String... arg0) {
			StringBuilder sb;
			try{
			sb = new StringBuilder(s.url+"getEvent/"+eventID);
			 String findlink=sb.toString();
	    	    HttpClient client=new DefaultHttpClient();
	    	   	    HttpGet request=new HttpGet();
	    	   		request.setURI(new URI(findlink)) ;
	    	   		HttpResponse response=client.execute(request);
	    	   	
	    	    	DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();	
	    	   	   DocumentBuilder Builder=factory.newDocumentBuilder();
	    	   	 Document  dom=Builder.parse(response.getEntity().getContent());
	    	   	  Node node=  dom.getDocumentElement();
	    	   	  
	    	   	 if(node.hasChildNodes())
	    	   	  {
	    	   	NodeList   nList5=dom.getElementsByTagName("Event");
	    	   	for(int temp=0;temp<nList5.getLength();temp++)
			  	{
			  		 Node nNode=nList5.item(temp);
			  		 if(nNode.getNodeType()==Node.ELEMENT_NODE)
			  		 {
			  			 		Element eElement=(Element)nNode;
			  			 		EventName = getTagValue("EventName",eElement);
			  			 	   CategoryName = getTagValue("CategoryName", eElement);
			  			 	   Location = getTagValue("Location",eElement)+", "+getTagValue("City",eElement)+" ,"+getTagValue("Zip",eElement);
			  			 	Date = getTagValue("Date",eElement);
			  			 	Time = getTagValue("Time",eElement);
			  			 	Price = getTagValue("Price", eElement);
			  			 	Temperature = getTagValue("MaxTemp",eElement)+" F/"+getTagValue("MinTemp",eElement)+" F";
			  			 	Weather = getTagValue("WeatherCondition",eElement);
			  			 	imageURL = getTagValue("WeatherImage", eElement);
			  			 	Desc = getTagValue("Description",eElement);
			  	      }
			  	}
	    	   	  }
	    	   	  else
	    	   	  {
	    	   		 // No search results
	    	   	  }
	    	   	 
           }
		
			catch(Exception e)
			{
				
			} 
			 
			return null;
		}
		private String getTagValue(String sTag,Element eElement)
		{
	    	NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
			
			if(nlList.getLength()>0)
			{
		    Node nValue = (Node) nlList.item(0);
	        return nValue.getNodeValue();
			}
			else
				return "";
		
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			 eventName = (TextView)findViewById(R.id.textView1);
			 desc = (TextView)findViewById(R.id.textView2);
			 location = (TextView)findViewById(R.id.textView3);
			 date = (TextView)findViewById(R.id.textView4);
			 time = (TextView)findViewById(R.id.textView5);
			 price = (TextView)findViewById(R.id.textView6);
			 temperature = (TextView)findViewById(R.id.textView7);
			 weather = (TextView)findViewById(R.id.textView8);
			categoryName = (TextView)findViewById(R.id.textView9);
			 weatherImage = (ImageView)findViewById(R.id.imageView1);
			ImageLoader imgLoader = new ImageLoader(getApplicationContext());
			Log.d("Image URL", imageURL);
			
			eventName.setText(EventName);
			desc.setText(Desc);
			categoryName.setText(CategoryName);
		 	   location.setText(Location);
		 	date.setText(Date);
		 	time.setText(Time);
		 	price.setText("$ "+Price);
		 	temperature.setText(Temperature);
		 	weather.setText(Weather);
		 	
			imgLoader.DisplayImage(imageURL, R.drawable.ic_action_name, weatherImage);
			
			super.onPostExecute(result);
		}
	}
	
	//Another AsyncTask for RSVP to event
	public class EventRSVP extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... params) {
			RestServiceUrl s = new RestServiceUrl();
				StringBuilder sb;
				try{
				sb = new StringBuilder(s.url+"userRSVP/"+Username+","+eventID);
				 String findlink=sb.toString();
		    	    HttpClient client=new DefaultHttpClient();
		    	   	    HttpGet request=new HttpGet();
		    	   		request.setURI(new URI(findlink)) ;
		    	   		HttpResponse response=client.execute(request);
		    	   	
		    	    	DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();	
		    	   	   DocumentBuilder Builder=factory.newDocumentBuilder();
		    	   	 Document  dom=Builder.parse(response.getEntity().getContent());
		    	   	  Element root=  dom.getDocumentElement();
		    	   	  rsvpStatus = root.getTextContent().toString();
			}
			catch(Exception e)
			{
				
			}
				
			return null;
			}
		@Override
		protected void onPostExecute(Void result) {
			AlertDialogManager alert = new AlertDialogManager();
			Log.d("RSVP status", rsvpStatus);
			if(rsvpStatus.equals("success"))
			{
				alert.showAlertDialog(EventHomePage.this, "Success", "You are attending the event!", true);
			}
			else
			{
				alert.showAlertDialog(EventHomePage.this, "Sorry", "You have already RSVPed to this event", false);
			}
			super.onPostExecute(result);
		}
		}
		
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_home_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(EventHomePage.this,Settings.class);
			startActivity(i);
			return true;
		}
		if( id == R.id.action_logout)
		{
			session = new SessionManager(getApplicationContext());
			session.logoutUser();
			return true;
		}
		if( id == R.id.action_home)
		{
			Intent i = new Intent(EventHomePage.this,HomeActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
}
