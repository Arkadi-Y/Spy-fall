package com.danielinc.spyfall;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class Lobby extends AppCompatActivity {
    TextView UNlbl,servCode;
    ArrayList<Player> playerList;
    MyAdapter adapter;
    Button Quit,Start;
    ListView PlayerListView;
    Intent intent;
    Player player;
    Host host;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        //setting up
        playerList=new ArrayList<>();
        setItems();
        isHost();
        setList();
    }
    public class MyAdapter extends BaseAdapter {
        ArrayList<Player> items;

        MyAdapter() {
            this.items = playerList;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater Linflater = getLayoutInflater();
            View view1 = Linflater.inflate(R.layout.rawdata_player, null);
            TextView username = view1.findViewById(R.id.Usernamelist);
            username.setText(items.get(i).getName());
            //Desc.setText(Double.toString(items.get(i).getAnswer()));
            return view1;
        }

        public void setItems(ArrayList<Player> items) {
            this.items = items;
        }
    }
    public void reSetList(){
        adapter.setItems(playerList);
    }
    public void setItems(){
        PlayerListView = findViewById(R.id.playerlist);
        UNlbl = findViewById(R.id.usernamelbl);
        servCode = findViewById(R.id.ServerCodeLbl);
        Quit = findViewById(R.id.QuitBtn);
        Start = findViewById(R.id.StartBtn);
        intent = getIntent();
    }
    public void isHost(){
        if(intent.getSerializableExtra("Host")!=null){
            setAdmin();
        }else{
            setPlayer();
        }
    }
    public void setPlayer(){
        player= (Player) intent.getSerializableExtra("Player");
        UNlbl.setText(getString(R.string.hostname)+" "+player.name);
        servCode.setText(getString(R.string.servercode)+" "+player.roomCode);
        adapter = new MyAdapter();
        setListeners();
    }
    public void setAdmin(){
        host = (Host) intent.getSerializableExtra("Host");
        UNlbl.setText(getString(R.string.hostname)+" "+host.name);
        servCode.setText(getString(R.string.servercode)+" "+host.roomCode);
        adapter = new MyAdapter();
        setHostListeners();
    }
    public void setList(){
        PlayerListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void setHostListeners(){
        listenToConnectingPlayers(host.roomCode);
        PlayerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //admin functions to players

            }
        });
        Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                host.EndGame();
                finish();
            }
        });
        Start.setOnClickListener(view -> {
<<<<<<< Updated upstream
            host.startGame();
            Intent intent = new Intent(this.getApplicationContext(),GameScreen.class);
            host.setRoles(playerList);
            intent.putExtra("playerList",playerList);
=======
            Intent intent = new Intent(getApplicationContext(),GameScreen.class);
            host.newRound(this.playerList);
            CRUD.UpdatePlayerRole(this.playerList,host.roomCode);
            intent.putExtra("Host",host);
>>>>>>> Stashed changes
            startActivity(intent);
        });
    }
    public void setListeners(){
            Quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CRUD.removePlayer(player.roomCode,player.name);
                    quitFunction();
                }
            });
        listenToConnectingPlayers(player.roomCode);
        listenToRoom(player.roomCode);
        listenToRoleChane(player.roomCode);
        }
    public void quitFunction(){
         finish();
        }
    public void instantKick(int i){
        //removing players here
        playerList.remove(i);
        //create crud kick
        adapter.notifyDataSetChanged();
    }
    public void setPlayerList(ArrayList<Player>list){
        this.playerList=list;
    }
    void listenToRoom(String roomCode){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference roomRef = database.getReference("rooms/" + roomCode);
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChildren())
                    quitFunction();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void listenToConnectingPlayers(String roomCode) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playerRef = database.getReference("rooms/" + roomCode + "/players");
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Player>newList=new ArrayList<>();
                for(DataSnapshot roleSnap : snapshot.getChildren()){
                    Log.d("connected",roleSnap.getKey());
                    newList.add(new Player(roleSnap.getKey(),roomCode));
                }
                setPlayerList(newList);
                setList();
                reSetList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
    void listenToRoleChane(String roomCode){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playerRef = database.getReference("rooms/" + roomCode + "/players/"+player.name);
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot roleSnap : snapshot.getChildren()){
                    if (roleSnap.getKey().equals(player.name)){
                        Intent intent = new Intent(getApplicationContext(),GameScreen.class);
                        player.setRole(roleSnap.getValue().toString());
                        intent.putExtra("Player",player);
                        Log.d("changed",roleSnap.getValue().toString());
                        startActivity(intent);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
    }
