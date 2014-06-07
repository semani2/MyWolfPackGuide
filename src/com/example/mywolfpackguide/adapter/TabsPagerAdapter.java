package com.example.mywolfpackguide.adapter;

import com.example.mywolfpackguide.AllEvents;
import com.example.mywolfpackguide.MyEvents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // All Events fragment activity
            return new AllEvents();
            
        case 1:
            // Events fragment activity
            return new AllEvents();
        case 2:
            // My Events fragment activity
            return new MyEvents();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}


