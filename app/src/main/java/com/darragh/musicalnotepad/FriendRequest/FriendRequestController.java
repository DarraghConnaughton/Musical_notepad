package com.darragh.musicalnotepad.FriendRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class FriendRequestController {
    public static void sendFriendRequest(final String UID, final String userName, final String currentUser, final String user){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                Map<String,Object> map = new HashMap<>();
                map.put(user+currentUser+"/pendingFriendRequest/"+UID +"/",userName);
                map.put(user+UID+"/FriendRequest/"+currentUser+"/","1");
                databaseReference.updateChildren(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    public static void RemoveFriendRequest(String UID, DatabaseReference databaseReference, String user){
        databaseReference.child(user+"/"+UID+"/pendingFriendRequest/"
                +FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);
        databaseReference.child(user+FirebaseAuth.getInstance().getCurrentUser()
                .getUid()+"/FriendRequest/"+UID).setValue(null);
    }

    public static void acceptFriendRequest(final String UID,final String userName, final int position, final String user){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Map<String,Object> map = new HashMap<>();
                map.put(user+FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()+"/FriendList/"+UID +"/",userName);
                map.put(user+UID+"/FriendList/"+FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()+"/",1);
                databaseReference.updateChildren(map);
                RemoveFriendRequest(UID, databaseReference, user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }
}
