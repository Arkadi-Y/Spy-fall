package com.danielinc.spyfall;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    ArrayList<String> locations;
    MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        setup();

    }
    public void startTimer(){
        myCountDownTimer = new MyCountDownTimer(10000, 1000);
        myCountDownTimer.start();
    }
    public void setup(){
        PlayerCard = findViewById(R.id.playerCard);
        SpyCard = findViewById(R.id.spyCard);
        intent=getIntent();
        LocationTxt = findViewById(R.id.LocationTxt);
        RoleText = findViewById(R.id.RoleTxt);
        LocationListView = findViewById(R.id.LocationList);
        adapter = new LocationListAdapter();
        locations = new ArrayList<>();
        isHost();
        selectView();


    }
    public void selectView(){
        if (role.equals("Spy")) {
            SpyCard.setVisibility(View.VISIBLE);
            loadLocationList();
        }
        else {
            PlayerCard.setVisibility(View.VISIBLE);
        }
    }

    public class LocationListAdapter extends BaseAdapter {
         ArrayList<String> items;
        LocationListAdapter() {
            items = new ArrayList<>();
            this.items = locations;
        }
        public void setItems(ArrayList<String>items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            if (this.items==null)
                return 1;
            return this.items.size();
        }

        @Override
        public String getItem(int i) {
            return this.items.get(i);
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
            LocationName.setText(locations.get(i));
            //Desc.setText(Double.toString(items.get(i).getAnswer()));
            return view1;
        }

    }
    public void setList(){
        LocationListView.setAdapter(adapter);
        adapter.setItems(locations);
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
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference locationRef = database.getReference("rooms/"+roomCode+"/location");
            locationRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                       location=task.getResult().toString();
                    }
                }
            });
        }
    public void loadLocationList(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference locationRef = database.getReference("/locations");
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    locations.add(data.child("title").getValue().toString());
                }
                setList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {

    }
    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished/1000);
            Log.d("timer",Integer.toString(progress));
        }

        @Override
        public void onFinish() {
            finish();
        }
    }
}