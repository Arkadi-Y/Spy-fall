package com.danielinc.spyfall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button config,Main,Rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        config = findViewById(R.id.config);
        Main = findViewById(R.id.mainWindow);
        Rules = findViewById(R.id.Rules);
        Main.setOnClickListener(v->{
            setMainWin(v);
        });
        config.setOnClickListener(v->{
            setConfig(v);
        });
        Rules.setOnClickListener(v->{
            setRules(v);
        });
        Main.callOnClick();
    }
    public void setMainWin(View v){
        Main.setVisibility(v.GONE);
        config.setVisibility(v.VISIBLE);
        Rules.setVisibility(v.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainLogin()).commit();
    }
    public void setConfig(View v){
        Main.setVisibility(v.VISIBLE);
        config.setVisibility(v.GONE);
        Rules.setVisibility(v.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Config()).commit();
    }
    public void setRules(View v){
        Main.setVisibility(v.VISIBLE);
        config.setVisibility(v.VISIBLE);
        Rules.setVisibility(v.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new game_rules_fragment()).commit();
    }
}