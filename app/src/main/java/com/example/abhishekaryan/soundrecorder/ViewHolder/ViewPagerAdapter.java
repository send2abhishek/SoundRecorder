package com.example.abhishekaryan.soundrecorder.ViewHolder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList=new ArrayList<>();
    private List<String> fragmentListTitle=new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentListTitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {


        return fragmentListTitle.get(position);
    }

    public void AddFragment(Fragment fragment,String title){


        fragmentList.add(fragment);
        fragmentListTitle.add(title);


    }
}
