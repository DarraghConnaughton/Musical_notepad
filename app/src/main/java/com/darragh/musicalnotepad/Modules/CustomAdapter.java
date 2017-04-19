package com.darragh.musicalnotepad.Modules;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.darragh.musicalnotepad.Pagers.Fragment1;
import com.darragh.musicalnotepad.Pagers.Fragment2;

public class CustomAdapter extends FragmentPagerAdapter {
    private String fragments [] = {"Fragment 1","Fragment 2"};

    public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return fragments[position];
    }
}
