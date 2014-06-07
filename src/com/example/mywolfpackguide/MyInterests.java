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

import com.example.mywolfpackguide.Login.LoginTask;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MyInterests extends Activity {
	private ListView lv;
	NewEventAdapter eAadapter;
	String Username = null, CategoryName = null;
	SessionManager session;
	Spinner spinner1;
	ArrayList<String> categories = new ArrayList<String>();
	Button add ;
	public String removeCateogryName = "";
	// Alert Dialog Manager
	   AlertDialogManager alert = new AlertDialogManager();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_interests);
		session = new SessionManager(getApplicationContext());
		Username = session.getUserDetails(); 
		add = (Button)findViewById(R.id.changePassword);
		spinner1 = (Spinner)findViewById(R.id.spinner1);
		add.setOnClickListener(new View.OnClickListener() {
	            
	           @Override
	           public void onClick(View arg0) {
	               CategoryName = String.valueOf(spinner1.getSelectedItem());
	                AddInterest a  = new AddInterest();
	                a.execute();
	           }
	       });
		MyTask m = new MyTask();
		m.execute();
		
	}

	//Async Task
	public class MyTask extends AsyncTask<String, Void, Void>
	{
		RestServiceUrl s = new RestServiceUrl();
		Element root ;
		String status = "";
		@Override
		protected Void doInBackground(String... params) {
			if(eAadapter != null)
				eAadapter.clearAdapter();
			StringBuilder sb;
			try{
			sb = new StringBuilder(s.url+"getMyInterests/"+Username);
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
	    	   	NodeList   nList5=dom.getElementsByTagName("Categories");
	    	   	for(int temp=0;temp<nList5.getLength();temp++)
			  	{
			  		 Node nNode=nList5.item(temp);
			  		 if(nNode.getNodeType()==Node.ELEMENT_NODE)
			  		 {
			  			 		Element eElement=(Element)nNode;
			  			 		categories.add(getTagValue("CategoryName", eElement));
			  			 	  
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
		
		@Override
		protected void onPostExecute(Void result) {
			 lv = (ListView) findViewById(R.id.listView1);
			 eAadapter = new NewEventAdapter(MyInterests.this,categories);
			 lv.setAdapter(eAadapter);
			 lv.setOnItemClickListener(new OnItemClickListener() {
		            public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		            	final TextView t1 = (TextView)view.findViewById(R.id.textView1);
		            	ImageView remove = (ImageView)view.findViewById(R.id.imageView2);
		            	final String catName = t1.getText().toString();
		            	remove.setOnClickListener(new OnClickListener(){

							public void onClick(View v){
								removeCateogryName = t1.getText().toString();
								Log.d("Remove Cat Name",removeCateogryName);
								RemoveInterest ri = new RemoveInterest();
								ri.execute();
							}
		            		
		            	});
		            	
		            	// Log.d("EventID", eventId);
		            	 
		            }

					
		        }); 
			super.onPostExecute(result);
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

		
	}
	
	public class RemoveInterest extends AsyncTask<String,Void,Void>
	{
		RestServiceUrl s = new RestServiceUrl();
		Element root ;
		String status = "";
		@Override
		protected Void doInBackground(String... params) {
			try{
				StringBuilder sb=new StringBuilder(s.url+"removeInterests/"+Username+","+removeCateogryName);
				String findlink=sb.toString();
				//HTTP request for Rest Service
				HttpClient client=new DefaultHttpClient();
	 	   	    HttpGet request=new HttpGet();
	 	   		request.setURI(new URI(findlink)) ;
	 	   		HttpResponse response=client.execute(request);
	 	   		//XML Parsing for response
	 	    	DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();	
	 	   	   	DocumentBuilder Builder=factory.newDocumentBuilder();
	 	   	   	Document  dom=Builder.parse(response.getEntity().getContent());
	 	   	   	dom.getDocumentElement().normalize();
	 	   	   	root=dom.getDocumentElement();
	 	   	   	status = root.getTextContent().toString();
			}
			catch(Exception e)
			{
				
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(MyInterests.this,"The category has been removed from your interests",Toast.LENGTH_LONG).show();
			MyTask m = new MyTask();
			m.execute();
			super.onPostExecute(result);
		}
		
		
	}
	
	public class AddInterest extends AsyncTask<String,Void,Void>
	{
		RestServiceUrl s = new RestServiceUrl();
		Element root ;
		String status = "";
		@Override
		protected Void doInBackground(String... params) {
			if(eAadapter != null)
				eAadapter.clearAdapter();
			try{
				StringBuilder sb=new StringBuilder(s.url+"addInterests/"+Username+","+CategoryName);
				String findlink=sb.toString();
				//HTTP request for Rest Service
				HttpClient client=new DefaultHttpClient();
	 	   	    HttpGet request=new HttpGet();
	 	   		request.setURI(new URI(findlink)) ;
	 	   		HttpResponse response=client.execute(request);
	 	   		//XML Parsing for response
	 	    	DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();	
	 	   	   	DocumentBuilder Builder=factory.newDocumentBuilder();
	 	   	   	Document  dom=Builder.parse(response.getEntity().getContent());
	 	   	   	dom.getDocumentElement().normalize();
	 	   	   	root=dom.getDocumentElement();
	 	   	   	status = root.getTextContent().toString();
			}
			catch(Exception e)
			{
				
			}
			return null;
		
		}
		
		//Post execute method
		@Override
		protected void onPostExecute(Void result) {
			if(status.equalsIgnoreCase("success"))
			{
				alert.showAlertDialog(MyInterests.this, "Interests Updated!", CategoryName+" has been added to your interests", true);
				MyTask m = new MyTask();
				m.execute();
				
			}
			else if(status.equalsIgnoreCase("already exists"))
			{
				alert.showAlertDialog(MyInterests.this, "Sorry!", CategoryName+" is already in your interests", false);
				MyTask m = new MyTask();
				m.execute();
			}
			else
			{
				alert.showAlertDialog(MyInterests.this, "Sorry!", "Please try again later", false);
				MyTask m = new MyTask();
				m.execute();
			}
			super.onPostExecute(result);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_interests, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(MyInterests.this,Settings.class);
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
			Intent i = new Intent(MyInterests.this,HomeActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
