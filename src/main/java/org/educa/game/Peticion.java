package org.educa.game;

import java.io.*;
import java.net.Socket;

public class Peticion implements Runnable {
    Socket socket;

    public Peticion(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream is = socket.getInputStream();
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader bfr = new BufferedReader(isr);
             OutputStream os = socket.getOutputStream();
             PrintWriter pw = new PrintWriter(os)) {

            String mensaje = bfr.readLine();
            String[] datosRecibidos = mensaje.split(",");
            String[] datosEnviados = Server.nuevoJugador(datosRecibidos[0], datosRecibidos[1]);
            if (datosEnviados != null){
                pw.println(arrayToCSV(datosEnviados));
            }else{
                pw.println("Error al encontrar partida: "+datosRecibidos[1]);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String arrayToCSV(String[] datos){
        return datos[0]+","+datos[1]+","+datos[2]+","+datos[3];
    }
}