package com.darragh.musicalnotepad.DatabaseEntries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.darragh.musicalnotepad.Objects.SongArrayObject;
import com.darragh.musicalnotepad.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SongListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> entries,timeSignature,keySignature,profilePicture,timestamp;

    public SongListAdapter(Context _context, SongArrayObject songArrayObject) {
        super(_context, R.layout.rowlayout ,songArrayObject.getListEntriesID());
        this.context = _context;
        this.entries = songArrayObject.getListEntriesName();
        this.timeSignature = songArrayObject.getListEntriesTimesignature();
        this.keySignature = songArrayObject.getListEntriesKeysignature();
        this.profilePicture = songArrayObject.getListEntriesProfilePhoto();
        this.timestamp = songArrayObject.getListEntriesTimestamp();
    }

    private String creationDate(long timestamp){
        Date date = new Date(timestamp);
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView keySign = (TextView) rowView.findViewById(R.id.key);
        TextView timeSign = (TextView) rowView.findViewById(R.id.time);
        TextView createdDate = (TextView) rowView.findViewById(R.id.creationDate);

        createdDate.setText(creationDate(Long.parseLong(timestamp.get(position))));
        textView.setText(entries.get(position));
        keySign.setText(keySignature.get(position));
        timeSign.setText(timeSignature.get(position));

        if(!profilePicture.get(position).equals("1")){
            ImageView imageView = (ImageView) rowView.findViewById(R.id.profilePhoto);
            Picasso.with(getContext()).load(profilePicture.get(position)+"?sz=65").into(imageView);
        }

        return rowView;
    }
}
