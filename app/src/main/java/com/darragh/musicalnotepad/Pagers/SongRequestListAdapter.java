package com.darragh.musicalnotepad.Pagers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.darragh.musicalnotepad.Pitch_Detector.Song;
import com.darragh.musicalnotepad.R;

import java.util.ArrayList;

/**
 * Created by darragh on 27/04/17.
 */

public class SongRequestListAdapter extends ArrayAdapter{
    private final Context context;
    private final ArrayList<Song> songList;


    public SongRequestListAdapter(Context _context, ArrayList<Song> _songList) {
        super(_context, R.layout.potentialsongrequestrow, _songList);
        this.context = _context;
        this.songList = _songList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.potentialsongrequestrow, parent, false);
        TextView label = (TextView) rowView.findViewById(R.id.label);
        TextView key = (TextView) rowView.findViewById(R.id.key);
        TextView time = (TextView) rowView.findViewById(R.id.time);
        label.setText(songList.get(position).getName());
        key.setText(songList.get(position).getKeySignature());
        time.setText(songList.get(position).getTimeSignature());
        return rowView;
    }
}
