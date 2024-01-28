package org.educa.game;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Server {

    private static Map<Integer, PartidaDados> partidasDados = new HashMap();
    private static int puertoLibre = 5555;

    public void run() {
        System.out.println("Creando socket servidor");
        iniciarServidor();
    }

    private void iniciarServidor(){
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
            try{
                if (newSocket != null) {
                    newSocket.close();
                }
            }catch (RuntimeException | IOException e){
                e.printStackTrace();
            }

        }
    }

    public static synchronized String[] nuevoJugador(String gameType, String nickName){
        System.out.println(puertoLibre);

        if (gameType.equalsIgnoreCase("dados")){
            int n_sala = 0;

            for (int i = 0; i < partidasDados.size(); i++) {
                n_sala++;
                if (!partidasDados.get(i).isFull()){
                    partidasDados.get(i).setP2(nickName, "localhost");
                    return new String[]{"localhost", String.valueOf(puertoLibre), nickName, "invitado"};
                }
            }
            System.out.println("Sala n"+n_sala+" creada");
            nuevoPuerto();
            partidasDados.put(n_sala,new PartidaDados(nickName, puertoLibre));
            return new String[]{"localhost", String.valueOf(puertoLibre), nickName, "anfitrion"};

        }
        return null;
    }

    private static synchronized void nuevoPuerto(){
        puertoLibre++;
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

    public static boolean getSalaLlena(String port, String tipoPartida){
        if(tipoPartida.equalsIgnoreCase("dados")){
            for (int i = 0; i < partidasDados.size(); i++) {
                if(partidasDados.get(i).getPort()==Integer.parseInt(port)){
                    return partidasDados.get(i).isFull();
                }
            }
        }
        return true;
    }

    public static String[] getDatosPartida(String[] datosJugador1, String tipoPartida){
        if(tipoPartida.equalsIgnoreCase("dados")){
            for (int i = 0; i < partidasDados.size(); i++) {
                if(partidasDados.get(i).getPort()==Integer.parseInt(datosJugador1[1])){
                    System.out.println(Arrays.toString(new String[]{datosJugador1[0], datosJugador1[1], datosJugador1[2]
                            , datosJugador1[3], partidasDados.get(i).getP2_host()
                            , String.valueOf(partidasDados.get(i).getPort())
                            , partidasDados.get(i).getP2_nickName()}));
                    return new String[] {datosJugador1[0],datosJugador1[1],datosJugador1[2]
                            ,datosJugador1[3],partidasDados.get(i).getP2_host()
                            ,String.valueOf(partidasDados.get(i).getPort())
                            ,partidasDados.get(i).getP2_nickName()};
                }
            }
        }
        return null;
    }

}