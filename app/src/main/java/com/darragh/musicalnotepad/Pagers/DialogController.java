package com.darragh.musicalnotepad.Pagers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.darragh.musicalnotepad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DialogController {
    private static String profilePhoto;
    private static String users;

    public static void onCreateDialog(final ArrayList<UserProfileDetails> listDetails, final Iterable<DataSnapshot> snap, Context context, final String profilePhoto) {
        final ArrayList<Integer> mSelectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] entries = getEmailAddresses(listDetails);
        builder.setTitle(R.string.dialog_title)
                .setMultiChoiceItems(entries, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SongSharer.shareSong(JSONToSongConverter.songFromJSON(snap),selectedFriends(mSelectedItems,listDetails),profilePhoto);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }

    public static void getFriendsList(final Iterable<DataSnapshot> snap, final Context context, String user){
        users=user;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                DialogController.onCreateDialog(gatherUsers(dataSnapshot),snap,context,profilePhoto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private static void setProfilePhoto(DataSnapshot data){
        if(data.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").exists()){
            profilePhoto = data.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePhoto/").getValue().toString();
        }
    }

    public static ArrayList<UserProfileDetails> gatherUsers(DataSnapshot dataSnapshot){
        setProfilePhoto(dataSnapshot);
        ArrayList<UserProfileDetails> usersFound = new ArrayList<>();
        Iterable<DataSnapshot> snap = dataSnapshot.child(users).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("/FriendList/").getChildren();
        for(DataSnapshot data: snap){
            usersFound.add(new UserProfileDetails(
                    dataSnapshot.child(users).child(data.getKey()).child("username").getValue().toString(),
                    dataSnapshot.child(users).child(data.getKey()).child("email").getValue().toString(),
                    data.getKey()));
        }
        return usersFound;
    }

    private static String[] getEmailAddresses(ArrayList<UserProfileDetails> listDetails){
        String[] emails = new String[listDetails.size()];
        for(int i=0; i<listDetails.size(); i++){
            emails[i] = listDetails.get(i).emailAddress;
        }
        return emails;
    }

    private static ArrayList<String> selectedFriends(ArrayList<Integer> mSelectedItems,ArrayList<UserProfileDetails> listDetails){
        ArrayList<String> selectedFriends = new ArrayList<>();

        for(int x: mSelectedItems){
            selectedFriends.add(listDetails.get(x).UID);
        }
        return selectedFriends;

    }

}
