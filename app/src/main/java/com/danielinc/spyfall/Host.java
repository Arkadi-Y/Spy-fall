package com.danielinc.spyfall;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Host implements Serializable {
    String name;
    String roomCode;
    String location;
    ArrayList roles;
    int locationNumber;


    public Host(String name){
        this.name = name;
        setRoomCode();
    }
    public void LoadGame(){
        getLocation();
    }

    public void newRound(ArrayList<Player> playerList){
        ReSetRoles(playerList);
        setRoles(playerList);
    }
    public void setPlayers(){
    };
    public void setRoomCode(){
        Random rand = new Random();
        String roomCode="";
        for(int i=0;i<5;i++){
            roomCode += (char)(rand.nextInt(90-65)+65);
        }
         this.roomCode=roomCode;
    };
    public void getLocation(){
        Random randomNum = new Random();
        this.locationNumber= randomNum.nextInt(49);
        this.location=CRUD.getLocation(this.locationNumber);
        this.roles = CRUD.getRoles(this.locationNumber);
    };
    public void setRoles(ArrayList<Player> playerList){
        Random r = new Random();
        for (Player p: playerList){
            if (p.role==null){
                int index = r.nextInt(this.roles.size());
                p.setRole((String) this.roles.get(index));
                this.roles.remove(index);
            }
        }
        setSpy(playerList);
    };

    public void ReSetRoles(ArrayList<Player> playerList){
        for (Player p: playerList){
            p.setRole(null);
        }
    }
    public void setSpy(ArrayList<Player> playerList){
        Random r = new Random();
        Log.d("list size -", String.valueOf(playerList.size()));
        playerList.get(r.nextInt(playerList.size())).setRole("Spy");
    };
    public void EndGame(){
        CRUD.closeRoom(this);
    }




}
