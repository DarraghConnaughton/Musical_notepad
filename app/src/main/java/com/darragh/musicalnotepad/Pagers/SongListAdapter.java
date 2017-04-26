package com.darragh.musicalnotepad.Pagers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.darragh.musicalnotepad.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class SongListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> entries,timeSignature,keySignature;

    public SongListAdapter(Context _context, ArrayList<String> _entries,
                           ArrayList<String> _timeSignature, ArrayList<String> _keySignature) {
        super(_context, R.layout.rowlayout ,_entries);
        this.context = _context;
        this.entries = _entries;
        this.timeSignature = _timeSignature;
        this.keySignature = _keySignature;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView keySign = (TextView) rowView.findViewById(R.id.key);
        TextView timeSign = (TextView) rowView.findViewById(R.id.time);
        textView.setText(entries.get(position));
        keySign.setText(keySignature.get(position));
        timeSign.setText(timeSignature.get(position));
        return rowView;
    }
}
