package com.darragh.musicalnotepad.Pagers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.darragh.musicalnotepad.Login_Register.SignIn;
import com.darragh.musicalnotepad.Pitch_Detector.Tuner;
import com.darragh.musicalnotepad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import be.tarsos.dsp.AudioDispatcher;

public class NavigationView_Details extends AppCompatActivity{
    private static ActionBarDrawerToggle actionBarDrawerToggle;
    private static int isRecording;

    public static void setProfilePicture(NavigationView navigationView, Context context){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        View headView = navigationView.getHeaderView(0);
        ImageView profileImage = (ImageView) headView.findViewById(R.id.profilePicture);
        TextView username = (TextView) headView.findViewById(R.id.userName);
        TextView email = (TextView) headView.findViewById(R.id.email);
        email.setText(firebaseUser.getEmail());
        for(UserInfo profile : firebaseUser.getProviderData()){
            Picasso.with(context).load(profile.getPhotoUrl()+"?sz=500").into(profileImage);
            username.setText(profile.getDisplayName());
        }
    }

    private static void setFirebaseListener(Activity activity,Context context){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            activity.finish();
            activity.startActivity(new Intent(context,SignIn.class));
        }
    }

    public static void incrementRecordCounter(){
        isRecording++;
    }

    private static void checkDispatcher(AudioDispatcher dispatcher){
        if(isRecording%2==1){
            dispatcher.stop();
        }
    }

    public static void setNavigationView(NavigationView navigationBar, final Context context, final Activity activity, final AudioDispatcher dispatcher, int recording){
        isRecording=recording;
        setFirebaseListener(activity,context);
        setProfilePicture(navigationBar,context);
        navigationBar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getTitle().equals("Record Audio")){
                    checkDispatcher(dispatcher);
                    activity.finish();
                    activity.startActivity(new Intent(context, MainActivity.class));
                }
                else if(item.getTitle().equals("Database Entries")){
                    checkDispatcher(dispatcher);
                    activity.finish();
                    activity.startActivity(new Intent(context, DatabaseEntries.class));
                }
                else if(item.getTitle().equals("Find Friends")){
                    checkDispatcher(dispatcher);
                    activity.finish();
                    activity.startActivity(new Intent(context, FindFriend.class));
                }
                else if(item.getTitle().equals("Tuner")){
                    checkDispatcher(dispatcher);
                    activity.finish();
                    activity.startActivity(new Intent(context, Tuner.class));
                }
                else if(item.getTitle().equals("Friend Requests")){
                    checkDispatcher(dispatcher);
                    activity.finish();
                    activity.startActivity(new Intent(context, FriendRequest.class));
                }
                else if(item.getTitle().equals("Friend List")){
                    checkDispatcher(dispatcher);
                    activity.finish();
                    activity.startActivity(new Intent(context, FriendList.class));
                }
                else if(item.getTitle().equals("Pending Songs")){
                    checkDispatcher(dispatcher);
                    activity.finish();
                    activity.startActivity(new Intent(context, songRequestList.class));
                }
                else if(item.getTitle().equals("Logout")){
                    checkDispatcher(dispatcher);
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    activity.finish();
                    firebaseAuth.getInstance().signOut();
                    activity.startActivity(new Intent(context, SignIn.class));
                }
                return true;
            }
        });
    }

}
