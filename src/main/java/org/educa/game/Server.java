package org.educa.game;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Server {

    private static Map<Integer, Game> partidasDados = new HashMap();
    private static int puertoLibre = 5570;

    public void run() {
        System.out.println("Creando socket servidor");
        iniciarServidor();
    }

    private void iniciarServidor() {
        System.out.println("Creando socket servidor");
        Socket newSocket = null;
        try (ServerSocket serverSocket = new ServerSocket()) {
            System.out.println("Realizando el bind");
            InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
            // Asigna el socket a una dirección y puerto
            serverSocket.bind(addr);
            System.out.println("Aceptando conexiones");
            while (true) {
                newSocket = serverSocket.accept();
                System.out.println("Conexion recibida");
                // Clase que implementa Runnable
                Peticion p = new Peticion(newSocket);
                // Creación y ejecución del hilo
                Thread hilo = new Thread(p);
                hilo.start();
                System.out.println("Esperando nueva conexión");
                //mostrarSalas();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (newSocket != null) {
                    newSocket.close();
                }
            } catch (RuntimeException | IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static synchronized String[] nuevoJugador(String gameType, String nickName) {

        if (gameType.equalsIgnoreCase("dados")) {
            int n_sala = 0;

            for (int i = 0; i < partidasDados.size(); i++) {
                if (!partidasDados.get(i).isFull()) {
                    partidasDados.get(i).addPlayer(nickName,"localHost", puertoLibre);
                    return new String[]{"localhost", String.valueOf(puertoLibre), nickName, "invitado",String.valueOf(n_sala)};
                }
                n_sala++;
            }
            System.out.println("Sala n" + n_sala + " creada");
            nuevoPuerto();
            partidasDados.put(n_sala, new Game(nickName, "localhost", puertoLibre, 2));
            return new String[]{"localhost", String.valueOf(puertoLibre), nickName, "anfitrion", String.valueOf(n_sala)};

        }
        return null;
    }

    private static synchronized void nuevoPuerto() {
        puertoLibre++;
    }


    public static boolean getSalaLlena(String id_sala, String tipoPartida) {
        if (tipoPartida.equalsIgnoreCase("dados")) {
            return partidasDados.get(Integer.parseInt(id_sala)).isFull();
        }
        return true;
    }

    public static String[] getDatosPartida(String[] datosJugador1, String tipoPartida) {
        if (tipoPartida.equalsIgnoreCase("dados")) {
            if (datosJugador1[3].equalsIgnoreCase("anfitrion")) {
                return new String[]{datosJugador1[0], datosJugador1[1], datosJugador1[2],datosJugador1[3], datosJugador1[4]
                        ,partidasDados.get(Integer.valueOf(datosJugador1[4])).getPlayersInfo().get(1)[1]
                        ,partidasDados.get(Integer.valueOf(datosJugador1[4])).getPlayersInfo().get(1)[2]
                        ,partidasDados.get(Integer.valueOf(datosJugador1[4])).getPlayersInfo().get(1)[0]};
            } else {
                System.out.println(datosJugador1[4]);
                return new String[]{datosJugador1[0], datosJugador1[1], datosJugador1[2],datosJugador1[3], datosJugador1[4]
                        ,partidasDados.get(Integer.valueOf(datosJugador1[4])).getPlayersInfo().get(0)[1]
                        ,partidasDados.get(Integer.valueOf(datosJugador1[4])).getPlayersInfo().get(0)[2]
                        ,partidasDados.get(Integer.valueOf(datosJugador1[4])).getPlayersInfo().get(0)[0]};
            }
        }

        return null;
    }


    public static void partidaFinalizada(int i){
        partidasDados.remove(i);
        System.out.println("Partida "+i+" eliminada");
    }
}