package com.danielinc.spyfall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GameScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
    }
/*
    public class MyAdapter extends BaseAdapter {
        ArrayList<String> items;
        MyAdapter() {
            //arraylist here
            this.items = null;
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
            View view1 = Linflater.inflate(R.layout.rawdata_location, null);
            TextView LocationName = view1.findViewById(R.id.LocationName);
            //שולף מתוך הרשימה
            LocationName.setText(items.get(i).getName());

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

    public void setList(){
        PlayerListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
*/
}