//package com.darragh.musicalnotepad.Pagers;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.MenuItem;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.darragh.musicalnotepad.R;
//import com.firebase.client.Firebase;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserInfo;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;
//
//public class UserProfile extends AppCompatActivity {
//    private TextView userName, emailAddress, databaseCount;
//    private EditText changeUserName;
//    private ImageView profileImage;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        Firebase.setAndroidContext(this);
//        setContentView(R.layout.userprofile);
//        setUpNavigationBar();
//        instantiateView();
//    }
//
//    private void instantiateView(){
//        userName = (TextView) findViewById(R.id.userName);
//        emailAddress = (TextView) findViewById(R.id.emailAddress);
//        changeUserName = (EditText) findViewById(R.id.enterUserName);
//        profileImage = (ImageView) findViewById(R.id.profilePicture);
//        databaseCount = (TextView) findViewById(R.id.databaseCount);
//        setUserInformation();
//    }
//
//    private void setDatabaseCount(){
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(final DataSnapshot dataSnapshot) {
//                int count=0;
//                Iterable<DataSnapshot> snap = dataSnapshot.child(getResources().getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getResources().getString(R.string.songId)).getChildren();
//                for(DataSnapshot data: snap){
//                    count++;
//                }
//                databaseCount.setText(getResources().getString(R.string.countDatabase) + Integer.toString(count));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError){}
//        });
//    }
//
//    private void setUserInformation(){
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = auth.getCurrentUser();
//        emailAddress.setText(getResources().getString(R.string.emailAddress).toString() + firebaseUser.getEmail());
//        changeUserName.setText(firebaseUser.getDisplayName());
//        for(UserInfo profile : firebaseUser.getProviderData()){
//            Picasso.with(this).load(profile.getPhotoUrl()+"?sz=500").into(profileImage);
//            changeUserName.setText(profile.getDisplayName());
//        }
//        userName.setText(getResources().getString(R.string.userName));
//        setDatabaseCount();
//    }
//
//    private void setUpNavigationBar(){
//        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        NavigationView navigationBar = (NavigationView) findViewById(R.id.navigationBar);
//        navigationBar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                System.out.println(item.getTitle());
//                if(item.getTitle().equals("Record Audio")){
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                }
//                else if(item.getTitle().equals("Database Entries")){
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), DatabaseEntries.class));
//                }
//                else if(item.getTitle().equals("User Profile")){
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
//                }
//                else if(item.getTitle().equals("Find Friends")){
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), FindFriend.class));
//                }
//                else if(item.getTitle().equals("Friend Requests")){
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), FriendRequest.class));
//                }
//                else if(item.getTitle().equals("Friend List")){
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), FriendList.class));
//                }
//                else if(item.getTitle().equals("Pending Songs")){
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), songRequestList.class));
//                }
//                else if(item.getTitle().equals("Logout")){
//                    System.out.println("LOGOUT!!!!!!");
//                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                    firebaseAuth.signOut();
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                }
//
//                return true;
//            }
//        });
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
//        //----NOT WORKING AT THE MOMENT----
//        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//        if(getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//        }
//    }
//}
