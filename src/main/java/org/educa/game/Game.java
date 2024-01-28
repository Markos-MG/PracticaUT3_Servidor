package org.educa.game;

import java.util.ArrayList;

public class Game {

    private boolean full;
    private int maxPlayers;

    private ArrayList<String[]> playersInfo = new ArrayList<>();

    public Game(String nickname, String host , int port, int maxPlayers) {
        this.full = false;
        this.maxPlayers = maxPlayers;
        playersInfo.add(new String[]{nickname,host,String.valueOf(port)});
    }

    public void addPlayer(String nickname, String host, int port){
        playersInfo.add(new String[]{nickname,host,String.valueOf(port)});
        if(playersInfo.size()==maxPlayers){
            this.full = true;
        }
    }

    public boolean isFull() {
        return full;
    }

    public ArrayList<String[]> getPlayersInfo() {
        return playersInfo;
    }
}
