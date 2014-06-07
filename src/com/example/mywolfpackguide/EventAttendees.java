package com.example.mywolfpackguide;

import java.net.URI;
import java.util.ArrayList;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.os.Build;

public class EventAttendees extends Activity {
	private ListView lv;
	EventAdapter eAadapter;
	String eventID = null;
	ArrayList<String> users = new ArrayList<String>();
	SessionManager session ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		session =new SessionManager(getApplicationContext());
		setContentView(R.layout.activity_event_attendees);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    eventID = extras.getString("eventID");
		    Log.d("Event Id in home page", eventID);
		}
		
		MyTask m =  new MyTask();
		m.execute();
	}
	
	//Async Task
	public class MyTask extends AsyncTask<String, Void, Void>
	{
		RestServiceUrl s = new RestServiceUrl();
		
		@Override
		protected Void doInBackground(String... params) {
			StringBuilder sb;
			try{
			sb = new StringBuilder(s.url+"getAttendees/"+eventID);
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
	    	   	NodeList   nList5=dom.getElementsByTagName("EventAttendees");
	    	   	for(int temp=0;temp<nList5.getLength();temp++)
			  	{
			  		 Node nNode=nList5.item(temp);
			  		 if(nNode.getNodeType()==Node.ELEMENT_NODE)
			  		 {
			  			 		Element eElement=(Element)nNode;
			  			 		users.add(getTagValue("Username", eElement));
			  			 	  
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
			 lv = (ListView) findViewById(R.id.listView1);
			 eAadapter = new EventAdapter(EventAttendees.this,users);
			 lv.setAdapter(eAadapter);
			super.onPostExecute(result);
		}

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_attendees, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(EventAttendees.this,Settings.class);
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
			Intent i = new Intent(EventAttendees.this,HomeActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
