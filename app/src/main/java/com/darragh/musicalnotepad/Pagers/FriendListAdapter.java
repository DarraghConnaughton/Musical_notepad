package com.darragh.musicalnotepad.Pagers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.darragh.musicalnotepad.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.friendlistrow, parent, false);
            profilePicture = (ImageView) rowView.findViewById(R.id.imageView);
            Picasso.with(getContext()).load(users.get(position).profileImageUri+"?sz=65").into(profilePicture);
            searchName = (TextView) rowView.findViewById(R.id.searchName);
            searchEmail = (TextView) rowView.findViewById(R.id.searchEmail);
            searchEmail.setText(users.get(position).emailAddress);
            searchName.setText(users.get(position).userName);
            return rowView;
        }
    }

