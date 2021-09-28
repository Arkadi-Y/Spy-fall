package com.danielinc.spyfall;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CRUD {
        static void CreateRoom(String roomCode, String host, int maxPlayers, int roundTime){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference roomRef = database.getReference("rooms/"+roomCode);
            roomRef.child("config").child("host").setValue(host);
            roomRef.child("config").child("max-players").setValue(maxPlayers);
            roomRef.child("config").child("round-time").setValue(roundTime);
            roomRef.child("players").child(host).setValue("null");
        }

        static void ConnectPlayer(String roomCode,String playerName){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference playerListRef = database.getReference("rooms/"+roomCode+"/players");
            playerListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                //check if name exists in room
                public void onDataChange(DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        if (data.getKey().equals(playerName)) {
                            Log.d("connect","connected");
                        } else {
                            playerListRef.child(playerName).setValue("null");
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException();
                }
            });
        }
        static boolean roomExists(String roomCode) {
            final boolean[] exists = {false};
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference roomRef = database.getReference("/rooms");;
            roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        if (data.getKey().equals(roomCode)) {
                            exists[0]=true;
                            Log.d("connect","connected");
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException();
                }
            });
            return exists[0];
        }
        static Set<String> getLocationList() {
            Set<String> locations=new HashSet<String>();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference locationRef = database.getReference("/locations");
            locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        locations.add(data.child("title").getValue().toString());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return locations;
        }
        //booleans dont work
        static boolean isConnected(String name){
            final boolean[] exists = {false};
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference roomRef = database.getReference("/rooms");;
            roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        if (data.getKey().equals(name)) {
                            exists[0]=true;
                            Log.d("connect","connected");
                        } else {
                            //do something iCf not exists
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException();
                }
            });
            return exists[0];
        }
        static void UpdatePlayerRole(ArrayList<Player> playerList,String roomCode){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference playerRef = database.getReference("/rooms/"+roomCode+"/players");
            for (Player player : playerList){
                playerRef.child(player.name).setValue(player.role);
            }
        }
        static void removePlayer(String roomCode,String name){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference playerRef = database.getReference("rooms/" + roomCode + "/players/"+name);
            playerRef.removeValue();
        }
        static String getLocation(int locationNumber){
            String location;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference locationRef = database.getReference("locations/"+locationNumber+"/title");
            location = locationRef.toString();
            return location;
        }
        static ArrayList getRoles(int locationNumber){
            ArrayList roles = new ArrayList();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference locationRef = database.getReference("locations/"+locationNumber+"/roles");
            locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot roleSnap : snapshot.getChildren())
                        roles.add(roleSnap);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException();
                }
            });
            return roles;
        }
        static ArrayList getPlayerList(String roomCode){
            ArrayList <Player> playerList = new ArrayList();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference playerRef = database.getReference("/rooms/"+roomCode+"/players");
            playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot roleSnap : snapshot.getChildren()){
                        Log.d("rolesnap",roleSnap.getKey());
                        playerList.add(new Player(roleSnap.getKey(),roomCode));}
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException();
                }
            });
            return playerList;

        }
        public static void roleChangeListener(Player player, String code){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference playerRef = database.getReference("rooms/" + code + "/players/"+player.name);
            playerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot roleSnap : snapshot.getChildren()){
                        if (roleSnap.getKey().equals(player.name)){
                             player.setRole(roleSnap.getValue().toString());
                             Log.d("changed",roleSnap.getValue().toString());
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException();
                }
            });
        }
        static void closeRoom(Host host){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference roomRef = database.getReference("rooms/" + host.roomCode);
            roomRef.removeValue();
        }

}
/*
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

myRef.setValue("Hello, World!");

myRef.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        String value = dataSnapshot.getValue(String.class);
        Log.d(TAG, "Value is: " + value);
        }

@Override
public void onCancelled(DatabaseError error) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", error.toException());
        }
        });*/