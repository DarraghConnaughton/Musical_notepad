package com.darragh.musicalnotepad.FriendRequest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.darragh.musicalnotepad.Objects.UserProfileDetails;
import com.darragh.musicalnotepad.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class FriendRequestListAdapter extends ArrayAdapter{
    private final Context context;
    private final ArrayList<UserProfileDetails> users;
    private LayoutInflater inflater;
    private String user;
    private View rowView;
    private TextView emailAddress,userName;
    private Button acceptRequest,declineRequest;

    public FriendRequestListAdapter(Context _context, ArrayList<UserProfileDetails> _users) {
        super(_context, R.layout.friendrequestrow ,_users);
        this.users = _users;
        this.context = _context;
    }

    private void setButton(final int position){
        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendRequestController.acceptFriendRequest(users.get(position).UID,users.get(position).userName,position,user);
                acceptRequest.setVisibility(View.INVISIBLE);
                declineRequest.setVisibility(View.INVISIBLE);
                users.remove(position);
                notifyDataSetChanged();
            }
        });
        declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendRequestController.RemoveFriendRequest(users.get(position).UID,FirebaseDatabase.getInstance().getReference(),user);
                users.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    private void setString(){
        user=getContext().getResources().getString(R.string.users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.friendrequestrow, parent, false);
        ImageView profilePicture = (ImageView) rowView.findViewById(R.id.profilePicture);
        setString();
        if(users.get(position).profileImageUri!=null){
            Picasso.with(getContext()).load(users.get(position).profileImageUri+"?sz=65").into(profilePicture);
        }
        userName = (TextView) rowView.findViewById(R.id.userName);
        emailAddress = (TextView) rowView.findViewById(R.id.emailAddress);
        acceptRequest = (Button) rowView.findViewById(R.id.acceptRequest);
        declineRequest = (Button) rowView.findViewById(R.id.declineRequest);
        setButton(position);
        emailAddress.setText(users.get(position).emailAddress);
        userName.setText(users.get(position).userName);
        return rowView;
    }
}

