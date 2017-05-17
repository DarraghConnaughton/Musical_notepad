package com.darragh.musicalnotepad.FriendFinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.darragh.musicalnotepad.FriendRequest.FriendRequestController;
import com.darragh.musicalnotepad.Objects.UserProfileDetails;
import com.darragh.musicalnotepad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


class FindFriendListAdapter extends ArrayAdapter {
        private final Context context;
        private final ArrayList<UserProfileDetails> users;
        private String user;
        private LayoutInflater inflater;
        private View rowView;
        private ImageView profilePicture;
        private TextView searchName;
        private TextView searchEmail;

        public FindFriendListAdapter(Context _context, ArrayList<UserProfileDetails> _users) {
            super(_context, R.layout.friendrow ,_users);
            this.users = _users;
            this.context = _context;
        }

    private void setButton(final int position){
        Button addFriend = (Button) rowView.findViewById(R.id.addFriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendRequestController.sendFriendRequest(users.get(position).UID,users.get(position).userName
                ,FirebaseAuth.getInstance().getCurrentUser().getUid(),user);
                users.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    private void setStrings(){
        user=getContext().getResources().getString(R.string.users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.friendrow, parent, false);
        setStrings();
        profilePicture = (ImageView) rowView.findViewById(R.id.imageView);
        setButton(position);
        Picasso.with(getContext()).load(users.get(position).profileImageUri+"?sz=65").into(profilePicture);
        searchName = (TextView) rowView.findViewById(R.id.searchName);
        searchEmail = (TextView) rowView.findViewById(R.id.searchEmail);
        searchEmail.setText(users.get(position).emailAddress);
        searchName.setText(users.get(position).userName);
        return rowView;
    }
}
