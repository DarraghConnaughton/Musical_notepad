package com.darragh.musicalnotepad.Pagers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.darragh.musicalnotepad.Login_Register.User;
import com.darragh.musicalnotepad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by darragh on 26/04/17.
 */

public class FriendListAdapter extends  ArrayAdapter{
     private final Context context;
        private final ArrayList<UserProfileDetails> users;

    private LayoutInflater inflater;
    private View rowView;
    private ImageView profilePicture;
    private TextView searchName;
    private TextView searchEmail;
    private Button addFriend;

        public FriendListAdapter(Context _context, ArrayList<UserProfileDetails> _users) {
            super(_context, R.layout.friendrow ,_users);
            this.users = _users;
            this.context = _context;
        }

        private boolean containsDirectory(DataSnapshot dataSnapshot){
            if(dataSnapshot.child(getContext().getResources().getString(R.string.users)+FirebaseAuth.getInstance().getCurrentUser()
                    .getUid()).child("/FriendRequests/").exists()){
                return true;
            }
            else {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                Map<String,Object> friendRequest = new HashMap<>();
//                databaseReference.child(getContext().getResources().getString(R.string.users)+FirebaseAuth.getInstance().getCurrentUser()
//                        .getUid()).setValue("/FriendRequests/");
                return true;

            }
        }

        private void sendFriendRequest(final String UID){
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    Map<String,Object> map = new HashMap<>();
                    if(containsDirectory(dataSnapshot)){
                        map.put(getContext().getResources().getString(R.string.users)+FirebaseAuth.getInstance().getCurrentUser()
                                .getUid()+"/FriendRequest/",UID);
                        databaseReference.updateChildren(map);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError){}
            });
        }

        private void setButton(final int position){
            addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendFriendRequest(users.get(position).UID);
                    System.out.println(users.get(position).userName);
                }
            });
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.friendrow, parent, false);
            profilePicture = (ImageView) rowView.findViewById(R.id.imageView);
            searchName = (TextView) rowView.findViewById(R.id.searchName);
            searchEmail = (TextView) rowView.findViewById(R.id.searchEmail);
            addFriend = (Button) rowView.findViewById(R.id.AddFriend);
            setButton(position);
            searchEmail.setText(users.get(position).emailAddress);
            searchName.setText(users.get(position).userName);
            return rowView;
        }
    }

