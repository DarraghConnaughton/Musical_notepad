package com.darragh.musicalnotepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by darragh on 12/03/17.
 */

public class customAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> dataSource;

    public customAdapter(Context cont, ArrayList<String> items){
        context=cont;
        dataSource=items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView text = (TextView) rowView.findViewById(R.id.placeName);
        text.setText("CHEEEEESEEEEEEE");
        return rowView;
    }
}
