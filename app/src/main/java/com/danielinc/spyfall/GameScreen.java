package com.danielinc.spyfall;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GameScreen extends AppCompatActivity {
    LinearLayout PlayerCard,SpyCard;
    LocationListAdapter adapter;
    TextView LocationTxt,RoleText;
    Intent intent;
    Player player;
    Host host;
    ListView LocationListView;
    String role,location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        adapter = new LocationListAdapter();
        setup();

    }
    public void setup(){
        PlayerCard = findViewById(R.id.playerCard);
        SpyCard = findViewById(R.id.spyCard);
        intent=getIntent();
        LocationTxt = findViewById(R.id.LocationTxt);
        RoleText = findViewById(R.id.RoleTxt);
        isHost();
        selectView();


    }
    public void selectView(){
        if (role.equals("Spy")) {
            SpyCard.setVisibility(View.VISIBLE);
            setList();
        }
        else {
            PlayerCard.setVisibility(View.VISIBLE);
        }
    }

    public class LocationListAdapter extends BaseAdapter {
        String [] locations;
        Set<String> items = new HashSet<>();
        LocationListAdapter() {
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE) ;
            items = sharedPref.getStringSet("Locations", items);
            locations = (String[]) items.toArray();
        }

        @Override
        public int getCount() {
            return this.locations.length;
        }

        @Override
        public String getItem(int i) {
            return this.locations[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater Linflater = getLayoutInflater();
            View view1 = Linflater.inflate(R.layout.rawdata_location, null);
            TextView LocationName = view1.findViewById(R.id.LocationName);
            //שולף מתוך הרשימה
            LocationName.setText(locations[i]);
            //Desc.setText(Double.toString(items.get(i).getAnswer()));
            return view1;
        }

    }
    public void setList(){
        LocationListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        this.role = player.getRole();
        getLocation(player.roomCode);
        RoleText.setText(player.role);
        LocationTxt.setText(location);
    }
    public void setAdmin(){
        host = (Host) intent.getSerializableExtra("Host");
        this.role = host.getRole();
        getLocation(host.roomCode);
        RoleText.setText(host.role);
        LocationTxt.setText(location);
    }
    public void getLocation(String roomCode){
        this.location=CRUD.getRoomLocation(roomCode);
    }


}