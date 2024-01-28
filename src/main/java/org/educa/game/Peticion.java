package org.educa.game;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import static java.lang.Thread.sleep;

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

            if (mensaje.split(",").length > 1) {
                String[] datosRecibidos = mensaje.split(",");
                String[] datosJugador = Server.nuevoJugador(datosRecibidos[0], datosRecibidos[1]);

                if (datosJugador != null){
                    if(datosJugador[3].equalsIgnoreCase("anfitrion")){
                        while (!Server.getSalaLlena(datosJugador[4],datosRecibidos[0])){
                            esperar(50);
                        }
                    }
                    String[] datosPartida = Server.getDatosPartida(datosJugador,datosRecibidos[0]);
                    if (datosPartida != null){
                        pw.println(arrayToCSV(datosPartida));
                    }else {
                        pw.println("error");
                    }
                }else{
                    pw.println("error");
                }

            }else{
                Server.partidaFinalizada(Integer.parseInt(mensaje));
            }





        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String arrayToCSV(String[] array){
        if(array != null){
            String stringCSV = array[0];
            for (int i = 1; i < array.length; i++) {
                stringCSV = stringCSV+","+array[i];
            }
            return stringCSV;
        }else{
            return "error";
        }
    }

    private void esperar(int tiempo) {
        try {
            sleep(tiempo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}