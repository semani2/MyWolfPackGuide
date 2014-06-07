package com.example.mywolfpackguide;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewEventAdapter extends BaseAdapter{
	private Activity activity;
	ArrayList<String> users = new ArrayList<String>();
	
	public NewEventAdapter(Activity a,ArrayList<String> username)
	{
		activity = a;
		users = username;
	}
	@Override
	public int getCount() {
		
		return users.size();
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
	            v = vi.inflate(R.layout.new_user_row, null);
	            
	            TextView t1 = (TextView) v.findViewById(R.id.textView1);
	            t1.setText(users.get(position));
		
		return v;
	}
	public void clearAdapter() {
		users.clear();
		
	}

}
