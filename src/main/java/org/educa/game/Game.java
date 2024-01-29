package org.educa.game;

import java.util.ArrayList;

public class Game {

    private boolean full; // boolean que mide si pa partida esta llena o no
    private final int maxPlayers; // Establece el numero maximo de jugadores por partida
    private ArrayList<String[]> playersInfo = new ArrayList<>(); //Lista con la info de los jugadores de la partida

    /**
     * Constructor de Game
     * @param nickname Nombre del anfitrion
     * @param host Host del anfitrion
     * @param port Puerto seleccionado
     * @param maxPlayers Maximos jugadores para ese tipo de juego
     */
    public Game(String nickname, String host , int port, int maxPlayers) {
        this.full = false;
        this.maxPlayers = maxPlayers;
        playersInfo.add(new String[]{nickname,host,String.valueOf(port)});//Se agrega a ese jugador
    }

    /**
     * Agrega a un jugador a la lista de jugadores de la partida
     * @param nickname Nombre del jugador
     * @param host Host del jugador
     * @param port Puerto seleccionado
     */
    public void addPlayer(String nickname, String host, int port){
        playersInfo.add(new String[]{nickname,host,String.valueOf(port)});
        if(playersInfo.size()==maxPlayers){//Si tras agregarlo, se llega al maximo de jugadores. full pasa a ser true
            this.full = true;
        }
    }

    /**
     * Getter de full
     * @return full
     */
    public boolean isFull() {
        return full;
    }

    /**
     * Getter de los jugadores de la partida
     * @return playersInfo
     */
    public ArrayList<String[]> getPlayersInfo() {
        return playersInfo;
    }
}
