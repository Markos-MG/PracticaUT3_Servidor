package org.educa.game;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static List<Partida> partidasDados = new ArrayList<>();

    public void run() {
        System.out.println("Creando socket servidor");
        inicializarServidores();
    }

    private void inicializarServidores(){
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
                mostrarSalas();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if (newSocket != null) {
                    newSocket.close();
                }
            }catch (RuntimeException | IOException e){
                e.printStackTrace();
            }

        }
    }

    public static synchronized void nuevoJugador(String gameType, String nickName){
        if (gameType.equalsIgnoreCase("dados")){
            boolean partidaNueva = true;
            for (int i = 0; i < partidasDados.size(); i++) {
                if (!partidasDados.get(i).isFull()){
                    partidaNueva = false;
                    partidasDados.get(i).setP2_nickName(nickName);
                }
            }
            if(partidaNueva){
                System.out.println("PARTIDA CREADAAAAAAAAA");
                partidasDados.add(new Partida(nickName));
            }
        }
    }

    private void mostrarSalas(){
        System.out.println("//////// SALAS /////////");
        for (int i = 0; i < partidasDados.size(); i++) {
            System.out.println("SALA "+i);
            System.out.println(partidasDados.get(i).getP1_nickName());
            System.out.println(partidasDados.get(i).getP2_nickName());
            System.out.println("----------");
        }
        System.out.println("/////////////////////////");
    }
}