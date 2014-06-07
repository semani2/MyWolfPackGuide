package com.example.mywolfpackguide;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter {
	
	private Activity activity;
	ArrayList<String> eventName= new ArrayList<String>();
	ArrayList<String> eventId = new ArrayList<String>();
	
	//Constructor
	public UserAdapter(Activity a,ArrayList<String> EventName,ArrayList<String> EventId)
	{
		activity = a;
		eventName = EventName;
		eventId = EventId;
	}
	
	@Override
	public int getCount() {
		
		return eventName.size();
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View v = convertView;
	     
			
		 LayoutInflater vi =
	                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.event_row, null);
	            
	            TextView t1 = (TextView)v.findViewById(R.id.textView1);
	            EditText et1 = (EditText)v.findViewById(R.id.editText1);
	            
	            t1.setText(eventName.get(position));
	            et1.setText(eventId.get(position));
		return v;
	}

}
