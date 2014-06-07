package com.example.mywolfpackguide;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private Activity activity;
	ArrayList<String> list1;
	ArrayList<String> list2;
	ArrayList<String> list3;
	ArrayList<String> list4;
	ArrayList<String> list5;
	ArrayList<String> list6;
	ArrayList<String> list7;
	ArrayList<String> list8;
	 public MyAdapter(Activity a, ArrayList<String> CateogryName, ArrayList<String> Date, ArrayList<String> EventId,ArrayList<String> EventName,ArrayList<String> Location,ArrayList<String> Time, ArrayList<String> City, ArrayList<String> Zip) {
		 list1 = CateogryName;
		 activity = a;
		 list2 = Date;
		 list3 = EventId;
		 list4 = EventName;
		 list5 = Location;
		 list6 = Time;
		 list7 = City;
		 list8 = Zip;
	 }

	@Override
	public int getCount() {
		
		return list1.size();
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}
	 
	public void clearAdapter()
	{
		list1.clear();
		list2.clear();
		list3.clear();
		list4.clear();
		list5.clear();
		list6.clear();
		list7.clear();
		list8.clear();
		//notifyDataSetChanged();
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View v = convertView;
	     
		
		 LayoutInflater vi =
	                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.list_row, null);
	            
	            TextView t1 = (TextView) v.findViewById(R.id.textView1);
	             TextView t2 = (TextView) v.findViewById(R.id.textView2);
	             TextView t3 = (TextView) v.findViewById(R.id.textView3);
	          
	            EditText et = (EditText) v.findViewById(R.id.editText1);
	            TextView t5 = (TextView) v.findViewById(R.id.textView5);
	            TextView t6 = (TextView) v.findViewById(R.id.textView6);
	          
	             // Displaying in list
	            t5.setText(list1.get(position));
	            t3.setText(list2.get(position));
	            et.setText(list3.get(position));
	        
	            t1.setText(list4.get(position));
	            
	            
	            t2.setText(list5.get(position)+", "+list7.get(position)+", "+list8.get(position));
	            t6.setText(list6.get(position));
	           
	            return v;
	            
	           
	}
	     
	}
	


