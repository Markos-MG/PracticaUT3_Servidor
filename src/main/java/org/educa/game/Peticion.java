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
            String[] datosJugador = mensaje.split(",");
            Server.nuevoJugador(datosJugador[0], datosJugador[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}