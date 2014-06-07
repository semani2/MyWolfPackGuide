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
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
 
public class AllEvents extends Fragment {
	private ListView lv;
	MyAdapter adapter;
	ArrayList<String> CategoryName = new ArrayList<String>();
	ArrayList<String> Date = new ArrayList<String>();
	ArrayList<String> EventId = new ArrayList<String>();
	ArrayList<String> EventName = new ArrayList<String>();
	ArrayList<String> Location = new ArrayList<String>();
	ArrayList<String> Time= new ArrayList<String>();
	View rootView;
	
   


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        rootView = inflater.inflate(R.layout.fragment_all_events, container, false);
         MyTask m = new MyTask();
         m.execute();
        return rootView;
    }
	
	//Async Task
	public class MyTask extends AsyncTask<String,Void,Void>
	{
		RestServiceUrl s = new RestServiceUrl();
		@Override
		protected Void doInBackground(String... arg0) {
			StringBuilder sb;
			try{
			sb = new StringBuilder(s.url+"allEvents");
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
		
			 AllEvents ae = new AllEvents();
			 lv = (ListView)rootView.findViewById(R.id.listView1);
		        adapter = new MyAdapter(ae.getActivity(),CategoryName,Date,EventId,EventName,Location,Time,Time,Time);
		        lv.setAdapter(adapter);
		        
		       /* lv.setOnItemClickListener(new OnItemClickListener() {
		            public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		            	EditText e = (EditText) view.findViewById(R.id.editText1);
		            	 String institute_id = e.getText().toString();
		            	 Intent i = new Intent(getApplicationContext(), InstituteHome.class);
		                 // sending data to new activity
		                 i.putExtra("ins_id",institute_id);
		                 startActivity(i);
		            }
		        }); */
		}
	}
}