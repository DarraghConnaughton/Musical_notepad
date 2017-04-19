package com.darragh.musicalnotepad;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by darragh on 06/03/17.
 */

public class CustomSwipeAdapter extends PagerAdapter {

    @Override
    public int getCount(){
        return 0;
    }

    @Override
    public boolean isViewFromObject(View v, Object o){
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        return super.instantiateItem(container,position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        super.destroyItem(container,position,object);
    }
}
