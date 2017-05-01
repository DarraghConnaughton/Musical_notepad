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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by darragh on 26/04/17.
 */

public class FriendListAdapter extends ArrayAdapter{
     private final Context context;
        private final ArrayList<UserProfileDetails> users;

    private LayoutInflater inflater;
    private View rowView;
    private ImageView profilePicture;
    private TextView searchName;
    private TextView searchEmail;

        public FriendListAdapter(Context _context, ArrayList<UserProfileDetails> _users) {
            super(_context, R.layout.friendlistrow ,_users);
            this.users = _users;
            this.context = _context;
        }

//        private void sendFriendRequest(final String UID, final String userName){
//            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(final DataSnapshot dataSnapshot) {
//                    FirebaseAuth auth = FirebaseAuth.getInstance();
//                    FirebaseUser firebaseUser = auth.getCurrentUser();
//                    Map<String,Object> map = new HashMap<>();
//                    map.put(getContext().getResources().getString(R.string.users)+FirebaseAuth.getInstance().getCurrentUser()
//                            .getUid()+"/pendingFriendRequest/"+UID +"/",userName);
////                    databaseReference.updateChildren(map);
//                    System.out.println(getContext().getResources().getString(R.string.users)+UID+"/FriendRequest/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()
//                            +"/");
//                    System.out.println(firebaseUser.getDisplayName());
//                    map.put(getContext().getResources().getString(R.string.users)+UID+"/FriendRequest/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()
//                            +"/","1");
//                    databaseReference.updateChildren(map);
//
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError){}
//            });
//        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.friendrow, parent, false);
            profilePicture = (ImageView) rowView.findViewById(R.id.imageView);
            Picasso.with(getContext()).load(users.get(position).profileImageUri+"?sz=65").into(profilePicture);
            searchName = (TextView) rowView.findViewById(R.id.searchName);
            searchEmail = (TextView) rowView.findViewById(R.id.searchEmail);
            searchEmail.setText(users.get(position).emailAddress);
            searchName.setText(users.get(position).userName);
            return rowView;
        }
    }

