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

import com.example.mywolfpackguide.EventHomePage.EventRSVP;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class SearchEvents extends Activity {
	public ListView lv;
	MyAdapter adapter;
	ArrayList<String> CategoryName = new ArrayList<String>();
	ArrayList<String> Date = new ArrayList<String>();
	ArrayList<String> EventId = new ArrayList<String>();
	ArrayList<String> EventName = new ArrayList<String>();
	ArrayList<String> Location = new ArrayList<String>();
	ArrayList<String> Time= new ArrayList<String>();
	ArrayList<String> City = new ArrayList<String>();
	ArrayList<String> Zip = new ArrayList<String>();
	String search_tag = "";
	EditText et;
	SessionManager session;
	AlertDialogManager alert = new AlertDialogManager();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_events);
		et = (EditText)findViewById(R.id.editText1);
		session = new SessionManager(getApplicationContext());
		Button searchButton = (Button)findViewById(R.id.changePassword);
		
		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) { 
				
				search_tag = et.getText().toString();
				Log.d("Seacrh Tag",search_tag);
				if(search_tag.matches(""))
				{
					alert.showAlertDialog(SearchEvents.this, "Sorry !", "Please enter a valid text to search", false);
					
				}
				else
				{
					MyTask m = new MyTask();
					m.execute();
				}
			}
			});
	}
	
	//Async Task
			public class MyTask extends AsyncTask<String,Void,Void>
			{
				RestServiceUrl s = new RestServiceUrl();
				@Override
				protected Void doInBackground(String... arg0) {
					
					if(adapter != null)
						adapter.clearAdapter();
					StringBuilder sb;
					try{
					sb = new StringBuilder(s.url+"searchEvents/"+search_tag);
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
			    	   		Log.d("search results",search_tag);
			    	   		TextView t= (TextView)findViewById(R.id.textView2);
			    	   		  
			    	   		  
			    	   		  t.setVisibility(1);
			    	   	NodeList   nList5=dom.getElementsByTagName("ListOfEvents");
			    	   	for(int temp=0;temp<nList5.getLength();temp++)
					  	{
					  		 Node nNode=nList5.item(temp);
					  		 if(nNode.getNodeType()==Node.ELEMENT_NODE)
					  		 {
					  			 		Element eElement=(Element)nNode;
					  			 	   CategoryName.add(getTagValue("CategoryName", eElement));
					  			 	   Date.add(getTagValue("Date",eElement));
					  			 	EventId.add(getTagValue("EventId",eElement));
					  			 	EventName.add(getTagValue("EventName",eElement));
					  			 	Location.add(getTagValue("Location", eElement));
					  			 	Time.add(getTagValue("Time",eElement));
					  			 	City.add(getTagValue("City",eElement));
					  			 	Zip.add(getTagValue("Zip",eElement));
					  	      }
					  	}
			    	   	  }
			    	   	  else
			    	   	  {
			    	   		  Log.d("No search results",search_tag);
			    	   		TextView t= (TextView)findViewById(R.id.textView2);
			    	   		  
			    	   		  t.setText("No search results found");
			    	   		  t.setVisibility(0);
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
					
				        adapter = new MyAdapter(SearchEvents.this,CategoryName,Date,EventId,EventName,Location,Time,City,Zip);
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
				}
			}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_events, menu);
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
		if( id == R.id.action_logout)
		{
			session = new SessionManager(getApplicationContext());
			session.logoutUser();
			return true;
		}
		if( id == R.id.action_home)
		{
			Intent i = new Intent(SearchEvents.this,HomeActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
