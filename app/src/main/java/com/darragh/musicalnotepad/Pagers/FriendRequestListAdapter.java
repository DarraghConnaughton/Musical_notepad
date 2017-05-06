package com.darragh.musicalnotepad.Pagers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.darragh.musicalnotepad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class FriendRequestListAdapter extends ArrayAdapter{
    private final Context context;
    private final ArrayList<UserProfileDetails> users;
    private LayoutInflater inflater;
    private View rowView;
    private TextView emailAddress,userName;
    private Button acceptRequest,declineRequest;

    public FriendRequestListAdapter(Context _context, ArrayList<UserProfileDetails> _users) {
        super(_context, R.layout.friendrequestrow ,_users);
        this.users = _users;
        this.context = _context;
        for(UserProfileDetails d: _users){
            System.out.println(d.userName + " - " + d.UID);
        }
    }


    private void RemoveFriendRequest(String UID, DatabaseReference databaseReference){
        databaseReference.child(getContext().getResources().getString(R.string.users)+"/"+UID+"/pendingFriendRequest/"
                +FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);
        databaseReference.child(getContext().getResources().getString(R.string.users)+FirebaseAuth.getInstance().getCurrentUser()
                .getUid()+"/FriendRequest/"+UID).setValue(null);
    }

    private void acceptFriendRequest(final String UID,final String userName, final int position){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Map<String,Object> map = new HashMap<>();
                map.put(getContext().getResources().getString(R.string.users)+FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()+"/FriendList/"+UID +"/",userName);
                map.put(getContext().getResources().getString(R.string.users)+UID+"/FriendList/"+FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()+"/",1);
                databaseReference.updateChildren(map);
                RemoveFriendRequest(UID, databaseReference);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void setButton(final int position){
        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptFriendRequest(users.get(position).UID,users.get(position).userName,position);
                acceptRequest.setVisibility(View.INVISIBLE);
                declineRequest.setVisibility(View.INVISIBLE);
                users.remove(position);
                notifyDataSetChanged();
            }
        });
        declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveFriendRequest(users.get(position).UID,FirebaseDatabase.getInstance().getReference());
                users.remove(position);
                notifyDataSetChanged();
            }
        });
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.friendrequestrow, parent, false);
        ImageView profilePicture = (ImageView) rowView.findViewById(R.id.profilePicture);
        if(users.get(position).profileImageUri!=null){
            Picasso.with(getContext()).load(users.get(position).profileImageUri+"?sz=65").into(profilePicture);
        }
        userName = (TextView) rowView.findViewById(R.id.userName);
        emailAddress = (TextView) rowView.findViewById(R.id.emailAddress);
        acceptRequest = (Button) rowView.findViewById(R.id.acceptRequest);
        declineRequest = (Button) rowView.findViewById(R.id.declineRequest);
        setButton(position);
        emailAddress.setText("Email Address:  " + users.get(position).emailAddress);
        userName.setText("User name:  " + users.get(position).userName);
        return rowView;
    }
}

