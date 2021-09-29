package com.danielinc.spyfall;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
    String roomCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        //setting up
        playerList=new ArrayList<>();
        setItems();
        isHost();
        setList();
        CRUD.setLobbyListeners(this,this.roomCode);
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
        adapter = new MyAdapter();
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
        roomCode = player.roomCode;
        servCode.setText(getString(R.string.servercode)+" "+roomCode);
        setListeners();
    }
    public void setAdmin(){
        host = (Host) intent.getSerializableExtra("Host");
        player = new Player(host.name,host.roomCode);
        host.LoadGame();
        roomCode=host.roomCode;
        UNlbl.setText(getString(R.string.hostname)+" "+host.name);
        servCode.setText(getString(R.string.servercode)+" "+host.roomCode);
        setHostListeners();
    }
    public void setList(){
        PlayerListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void setHostListeners(){
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
            if (playerList.size()<=1){
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Alert");
                builder.setMessage("No players are connected");
                builder.show();
            }else{
                host.newRound(this.playerList);
                CRUD.UpdatePlayerRole(this.playerList,host.roomCode);
                CRUD.setLocation(host.roomCode,host.location);
                intent.putExtra("Host",host);
                CRUD.changeGameStatus(this.roomCode,"Game");
                }
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Pause ","HALT!!!");
    }
    Boolean flag=true;
    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to quit");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (host!=null)
                        host.EndGame();
                    else
                        CRUD.removePlayer(player.roomCode,player.name);
                    finish();
                    }

            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();

    }

}
