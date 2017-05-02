package com.darragh.musicalnotepad.Pagers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.darragh.musicalnotepad.Login_Register.SignIn;
import com.darragh.musicalnotepad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class NavigationView_Details extends AppCompatActivity{
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public static void setProfilePicture(NavigationView navigationView, Context context){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        View headView = navigationView.getHeaderView(0);
        ImageView profileImage = (ImageView) headView.findViewById(R.id.profilePicture);
        TextView username = (TextView) headView.findViewById(R.id.userName);
        TextView email = (TextView) headView.findViewById(R.id.email);
        email.setText(firebaseUser.getEmail());
        System.out.println(username + " - " + email + " - " + profileImage);
        for(UserInfo profile : firebaseUser.getProviderData()){
            Picasso.with(context).load(profile.getPhotoUrl()+"?sz=500").into(profileImage);
            username.setText(profile.getDisplayName());
        }
    }

    private static void setActionBarDetails(DrawerLayout mDrawerLayout, Activity activity){
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        ((AppCompatActivity)activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)activity).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        actionBarDrawerToggle.syncState();
    }

    private static void setFirebaseListener(Activity activity,Context context){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            activity.finish();
            activity.startActivity(new Intent(context,SignIn.class));
        }
    }

    public static void setNavigationView(NavigationView navigationBar, final Context context, final Activity activity, DrawerLayout mDrawerLayout){
        setFirebaseListener(activity,context);
        setProfilePicture(navigationBar,context);
        setActionBarDetails(mDrawerLayout,activity);
        navigationBar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getTitle().equals("Record Audio")){
                    activity.finish();
                    activity.startActivity(new Intent(context, MainActivity.class));
                }
                else if(item.getTitle().equals("Database Entries")){
                    activity.finish();
                    activity.startActivity(new Intent(context, DatabaseEntries.class));
                }
                else if(item.getTitle().equals("Find Friends")){
                    activity.finish();
                    activity.startActivity(new Intent(context, FindFriend.class));
                }
                else if(item.getTitle().equals("Friend Requests")){
                    activity.finish();
                    activity.startActivity(new Intent(context, FriendRequest.class));
                }
                else if(item.getTitle().equals("Friend List")){
                    activity.finish();
                    activity.startActivity(new Intent(context, FriendList.class));
                }
                else if(item.getTitle().equals("Pending Songs")){
                    activity.finish();
                    activity.startActivity(new Intent(context, songRequestList.class));
                }
                else if(item.getTitle().equals("Logout")){
                    System.out.println("LOGOUT!!!!!!");
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    activity.finish();
                    firebaseAuth.getInstance().signOut();
                    activity.startActivity(new Intent(context, SignIn.class));
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return actionBarDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item)||actionBarDrawerToggle.onOptionsItemSelected(item);
    }

}
