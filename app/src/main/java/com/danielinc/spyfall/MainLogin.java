package com.danielinc.spyfall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainLogin extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    Button Create,Join;
    TextView username ,roomCode;

    public MainLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainLogin.
     */

    public static MainLogin newInstance(String param1, String param2) {
        MainLogin fragment = new MainLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_login, container, false);
        setItems(view);
        setListeners();
        CreateDefaultConfig();

        loadUsername();
        return view;
    }
   public void CreateDefaultConfig(){
        Context context = this.getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.sharedpref),Context.MODE_PRIVATE);
        if(sharedPref.getAll().toString() == "{}"){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("RoundTime",5).commit();
            editor.putInt("NumOfPlayers",7).commit();
        }
    }
   public void setItems(View view){
       Create = view.findViewById(R.id.CreateBtn);
       Join = view.findViewById(R.id.LoginBtn);
       roomCode = view.findViewById(R.id.ServerTxt);
       username = view.findViewById(R.id.UsernameTxt);
       username.setText("");
   }
   public void saveUsername(){
       SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.sharedpref),getActivity().getApplicationContext().MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putString("username",username.getText().toString()).commit();
   }
   public void loadUsername(){

       SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.sharedpref),getActivity().getApplicationContext().MODE_PRIVATE);
       username.setText(sharedPreferences.getString("username",null));
   }
   public void joinFunction(){
       Intent intent = new Intent(getActivity().getApplicationContext(),Lobby.class);
       String userName = username.getText().toString();
       String Code = roomCode.getText().toString();
       final boolean[] exists = {false};
       if(userName.length()>3) {
           saveUsername();
           FirebaseDatabase database = FirebaseDatabase.getInstance();
           DatabaseReference roomRef = database.getReference("/rooms");;
           roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot snapshot) {
                   for(DataSnapshot data: snapshot.getChildren()){
                       Log.d("Looking",data.getKey()+" code -"+Code);
                       if (data.getKey().equals(Code)) {
                           CRUD.ConnectPlayer(Code,userName);
                           Player player = new Player(userName,Code);
                           exists[0] = true;
                           Log.d("connect","connected");
                           intent.putExtra("Player",player);
                           startActivity(intent);

                           break;
                       }
                   } if (!exists[0])
                       Toast.makeText(getActivity().getApplicationContext(),"No room found" ,Toast.LENGTH_SHORT).show();
               }
               @Override
               public void onCancelled(@NonNull DatabaseError error) {
                   throw error.toException();
               }
           });

       }else{ Toast.makeText(getActivity().getApplicationContext(),getString(R.string.UsernameAlert) ,Toast.LENGTH_SHORT).show(); }
   }
   public void createFunction(){
       Context context = this.getContext();
       SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.sharedpref),Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPref.edit();
       Intent intent = new Intent(getActivity().getApplicationContext(),Lobby.class);
       if(username.getText().toString().length()>3){
           saveUsername();
           Host host = new Host(username.getText().toString());
           //TODO: creating server in firebase here
           CRUD.CreateRoom(host.roomCode,host.name,sharedPref.getInt("NumOfPlayers",1),sharedPref.getInt("RoundTime",1));
           intent.putExtra("Host",host);
           startActivity(intent);
       }
       else{
           Toast.makeText(getActivity().getApplicationContext(),getString(R.string.UsernameAlert) ,Toast.LENGTH_SHORT).show();
       }
   }
   public void setListeners(){
        Create.setOnClickListener(v->{
            createFunction();
       });
       Join.setOnClickListener(v->{
           joinFunction();
          });

   }

}