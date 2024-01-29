package org.educa.game;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Server {

    private static Map<Integer, Game> dadosGames = new HashMap();
    private static int freePort = 5555;

    public void run() {
        System.out.println("Creando socket servidor");
        initializeServer();
    }

    private void initializeServer() {
        Socket newSocket = null;
        try (ServerSocket serverSocket = new ServerSocket()) {
            InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
            serverSocket.bind(addr);
            while (true) {
                newSocket = serverSocket.accept();
                System.out.println("\tConexion recibida");
                Request p = new Request(newSocket);
                Thread hilo = new Thread(p);
                hilo.start();
                //System.out.println("Esperando nueva conexión");
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error en la conexion");
        } finally {
            try {
                if (newSocket != null) {
                    newSocket.close();
                }
            } catch (RuntimeException | IOException e) {
                //e.printStackTrace();
                System.out.println("Error al cerrar la conexion");
            }

        }
    }

    public static synchronized String[] newPlayer(String gameType, String nickName) {

        if (gameType.equalsIgnoreCase("dados")) {
            int roomNum = 0;

            for (int i = 0; i < dadosGames.size(); i++) {
                if (!dadosGames.get(i).isFull()) {
                    dadosGames.get(i).addPlayer(nickName,"localHost", freePort);
                    return new String[]{"localhost", String.valueOf(freePort), nickName, "invitado",String.valueOf(roomNum)};
                }
                roomNum++;
            }
            System.out.println("Sala n" + roomNum + " creada");
            newPort();
            dadosGames.put(roomNum, new Game(nickName, "localhost", freePort, 2));
            return new String[]{"localhost", String.valueOf(freePort), nickName, "anfitrion", String.valueOf(roomNum)};

        }
        return null;
    }

    private static synchronized void newPort() {
        freePort++;
    }


    public static boolean getFullGame(String id_sala, String tipoPartida) {
        if (tipoPartida.equalsIgnoreCase("dados")) {
            return dadosGames.get(Integer.parseInt(id_sala)).isFull();
        }
        return true;
    }

    public static String[] getGameData(String[] playerData, String gameType) {
        if (gameType.equalsIgnoreCase("dados")) {
            if (playerData[3].equalsIgnoreCase("anfitrion")) {
                return new String[]{playerData[0], playerData[1], playerData[2],playerData[3], playerData[4]
                        , dadosGames.get(Integer.valueOf(playerData[4])).getPlayersInfo().get(1)[1]
                        , dadosGames.get(Integer.valueOf(playerData[4])).getPlayersInfo().get(1)[2]
                        , dadosGames.get(Integer.valueOf(playerData[4])).getPlayersInfo().get(1)[0]};
            } else {
                return new String[]{playerData[0], playerData[1], playerData[2],playerData[3], playerData[4]
                        , dadosGames.get(Integer.valueOf(playerData[4])).getPlayersInfo().get(0)[1]
                        , dadosGames.get(Integer.valueOf(playerData[4])).getPlayersInfo().get(0)[2]
                        , dadosGames.get(Integer.valueOf(playerData[4])).getPlayersInfo().get(0)[0]};
            }
        }

        return null;
    }


    public static void gameConcluded(int i){
        dadosGames.remove(i);
        System.out.println("Sala n"+i+" eliminada");
    }
}