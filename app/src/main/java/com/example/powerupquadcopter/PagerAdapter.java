package com.example.powerupquadcopter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;

/**
 * Created by Chirag on 30-Jul-17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                Tab_Control tab0 = new Tab_Control();
                return tab0;
            case 1:
                Tab_Camera tab1 = new Tab_Camera();
                return tab1;
            case 2:
                Tab_GPS tab2 = new Tab_GPS();
                return tab2;
            case 3:
                Tab_Network tab3 = new Tab_Network();
                return tab3;
            case 4:
                Tab_Settings tab4 = new Tab_Settings();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}