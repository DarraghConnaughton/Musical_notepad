package com.darragh.musicalnotepad.Pagers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.darragh.musicalnotepad.Pitch_Detector.Song;
import com.darragh.musicalnotepad.R;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongRequestListAdapter extends ArrayAdapter{
    private final Context context;
    private final ArrayList<Song> songList;
    private String user;
    private TextView songName,key,time;
    private ImageView profilePicture;
    private Button previewSong,acceptSong,declineSong;
    private View mainView;

    public SongRequestListAdapter(Context _context, ArrayList<Song> _songList, View _mainView) {
        super(_context, R.layout.potentialsongrequestrow, _songList);
        this.context = _context;
        this.songList = _songList;
        this.mainView = _mainView;
    }

//    private void addSongToSongList(final String timeStamp){
//        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(final DataSnapshot dataSnapshot) {
//                Map<String,Object> map = new HashMap<>();
//                Song sentSong = JSONToSongConverter.songFromJSON(dataSnapshot.child(user+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/SongRequest/"+timeStamp+"/").getChildren());
//
//                map.put(user+ FirebaseAuth.getInstance().getCurrentUser()
//                        .getUid()+"/songId/"+timeStamp+"/",sentSong);
//                databaseReference.updateChildren(map);
//                SongRequestListController.RemoveSongRequest(timeStamp, databaseReference, sentSong.getUID(),user);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError){}
//        });
//    }

//    private void RemoveSongRequest(String timeStamp,DatabaseReference databaseReference, String UID){
//        databaseReference.child(user+UID+"/PendingSong/"+timeStamp)
//                .setValue(null);
//        databaseReference.child(user+FirebaseAuth.getInstance()
//                .getCurrentUser().getUid()+"/SongRequest/"+timeStamp).setValue(null);
//    }

    private void previewSong(View rowView, final String timeStamp){
        Intent intent = new Intent(getContext(),songDisplay.class);
        intent.putExtra("Timestamp",timeStamp);
        intent.putExtra("Directory","/SongRequest/");
        mainView.getContext().startActivity(intent);
    }

    private void setButtons(final View rowView, final int position){
        previewSong = (Button) rowView.findViewById(R.id.preview);
        previewSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewSong(rowView, songList.get(position).getTimestamp());
            }
        });
        acceptSong = (Button) rowView.findViewById(R.id.accept);
        acceptSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongRequestListController.addSongToSongList(songList.get(position).getTimestamp(),user);
                songList.remove(position);
                notifyDataSetChanged();
            }
        });
        declineSong = (Button) rowView.findViewById(R.id.decline);
        declineSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongRequestListController.RemoveSongRequest(songList.get(position).getTimestamp()
                        ,FirebaseDatabase.getInstance().getReference()
                        ,songList.get(position).getUID(),user);
                songList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.potentialsongrequestrow, parent, false);
        songName = (TextView) rowView.findViewById(R.id.songName);
        key = (TextView) rowView.findViewById(R.id.key);
        time = (TextView) rowView.findViewById(R.id.time);
        profilePicture = (ImageView) rowView.findViewById(R.id.userProfilePicture);

        setButtons(rowView,position);
        setValues(position);

        return rowView;
    }

    private void setValues(int position){
        user=getContext().getResources().getString(R.string.users);
        songName.setText(songList.get(position).getName());
        key.setText(songList.get(position).getKeySignature());
        time.setText(songList.get(position).getTimeSignature());
        previewSong.setText(getContext().getResources().getString(R.string.previewSong));
        if(!songList.get(position).getProfilePhoto().equals(null)){
            Picasso.with(getContext()).load(songList.get(position).getProfilePhoto()+"?sz=350").into(profilePicture);
        }
    }
}
