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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class UserEvents extends Activity {
	SessionManager session;
	private ListView lv;
	UserAdapter adapter;
	ArrayList<String> EventId = new ArrayList<String>();
	ArrayList<String> EventName = new ArrayList<String>();
	String username= null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_events);
		session = new SessionManager(getApplicationContext());
		username = session.getUserDetails();
	/*	TODO
	 Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    username = extras.getString("Username");
		    Log.d("Event Id in home page", username);
		} */
		MyTask m = new MyTask();
		m.execute();
	}
	
	public class MyTask extends AsyncTask<String,Void,Void>
	{
		RestServiceUrl s = new RestServiceUrl();
		@Override
		protected Void doInBackground(String... params) {
			StringBuilder sb;
			try{
			sb = new StringBuilder(s.url+"attendingEvents/"+username);
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
	    	   	NodeList   nList5=dom.getElementsByTagName("UserEvents");
	    	   	Log.d("No of elemtns",String.valueOf(nList5.getLength()));
	    	   	for(int temp=0;temp<nList5.getLength();temp++)
			  	{
	    	   		
			  		 Node nNode=nList5.item(temp);
			  		 if(nNode.getNodeType()==Node.ELEMENT_NODE)
			  		 {
			  			 		Element eElement=(Element)nNode;
			  			 		EventId.add(getTagValue("EventId", eElement));
			  			 		EventName.add(getTagValue("EventName",eElement)); 	   
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
	        adapter = new UserAdapter(UserEvents.this, EventName, EventId);
	        lv.setAdapter(adapter);
	        lv.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	            	EditText e = (EditText) view.findViewById(R.id.editText1);
	            	 String eventId = e.getText().toString();
	            	 Log.d("EventID", eventId);
	            	 Intent i = new Intent(getApplicationContext(), EventHomePage.class);
	                 // sending data to new activity
	                 i.putExtra("eventID",eventId);
	                 startActivity(i);
	            }

				
	        }); 
			super.onPostExecute(result);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_events, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(UserEvents.this,Settings.class);
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
			Intent i = new Intent(UserEvents.this,HomeActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



}
