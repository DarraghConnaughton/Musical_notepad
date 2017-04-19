package com.darragh.musicalnotepad;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

/**
 * Created by darragh on 06/03/17.
 */

public class PagerControl extends AppCompatActivity{

    private ViewPager viewPager;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagerlayout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(),getApplicationContext()));

    }
}
