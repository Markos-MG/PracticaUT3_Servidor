package org.educa.game;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Server {

    private static Map<Integer, Game> dadosGames = new HashMap(); //HasMap donde se guarda las partidas en curso
    private static int freePort = 5555;//Guarda el primer puerto disponible

    /**
     * Metodo run de la clase Server.
     * Llama al metodo de initializeServer()
     */
    public void run() {
        System.out.println("Creando socket servidor");
        initializeServer();
    }

    /**
     * Inicia el servidor
     */
    private void initializeServer() {
        Socket newSocket = null;
        try (ServerSocket serverSocket = new ServerSocket()) {
            //Puerto y direccion que usaremos para recibir a los clientes
            InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
            //Lo asignamos
            serverSocket.bind(addr);
            while (true) {//Buque que permite recibir todas las peticiones
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
                    newSocket.close();//Cerramos la conexion
                }
            } catch (RuntimeException | IOException e) {
                //e.printStackTrace();
                System.out.println("Error al cerrar la conexion");
            }
        }
    }

    /**
     * Metodo que asigna una partida al jugador que recibe
     * @param gameType Tipo de juego
     * @param nickName Nombre del jugador
     * @return array con la informacion del jugador
     */
    public static synchronized String[] newPlayer(String gameType, String nickName) {
        if (gameType.equalsIgnoreCase("dados")) {
            int roomNum = 0;//Contador para las salas disponibles/creadas

            for (int i = 0; i < dadosGames.size(); i++) {//Recorro las salas y busco que no esten llenas
                if (!dadosGames.get(i).isFull()) {
                    dadosGames.get(i).addPlayer(nickName,"localHost", freePort);//Agrego al jugador
                    //Devuelvo la informacion del jugador
                    return new String[]{"localhost", String.valueOf(freePort), nickName, "invitado",String.valueOf(roomNum)};
                }
                roomNum++;
            }
            //Si no encuentra ninguna sala que no este llena, crea una
            System.out.println("Sala n" + roomNum + " creada");
            newPort();//Cambia el puerto disponible
            dadosGames.put(roomNum, new Game(nickName, "localhost", freePort, 2));
            //Devuelvo la informacion del jugador
            return new String[]{"localhost", String.valueOf(freePort), nickName, "anfitrion", String.valueOf(roomNum)};

        }
        return null;
    }

    /**
     * Metodo sincronizado que aumenta en 1 el puerto libre para tener uno nuevo
     */
    private static synchronized void newPort() {
        freePort++;
    }


    /**
     * Metodo que permite saber si una sala esta llena
     * @param id_sala id de la sala que se desea buscar
     * @param tipoPartida tipo de juego
     * @return true o false dependiendo de si esta llena
     */
    public static boolean getFullGame(String id_sala, String tipoPartida) {
        if (tipoPartida.equalsIgnoreCase("dados")) {
            return dadosGames.get(Integer.parseInt(id_sala)).isFull();
        }
        return true;
    }

    /**
     * Metodo que devuelve toda la informacion de una partida
     * @param playerData info del jugador
     * @param gameType tipo de partida
     * @return informacion de la partida
     */
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


    /**
     * Metodo que elimina la partida buscada
     * @param id el id de la partida
     */
    public static void gameConcluded(int id){
        dadosGames.remove(id);
        System.out.println("Sala n"+id+" eliminada");
    }
}