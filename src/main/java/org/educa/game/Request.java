package org.educa.game;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Request implements Runnable {
    Socket socket;

    /**
     * Constructor de la peticion
     * @param socket El socket de Server
     */
    public Request(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream is = socket.getInputStream();
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader bfr = new BufferedReader(isr);
             OutputStream os = socket.getOutputStream();
             PrintWriter pw = new PrintWriter(os)) {

            //Le el primer mensaje (tipo de juego y nickName)
            String message = bfr.readLine();

            if (message.split(",").length > 1) {//Si tiene el formato requerido, continua
                String[] dataReceived = message.split(",");
                //Busca una partida y recibe la informacion
                String[] playerData = Server.newPlayer(dataReceived[0], dataReceived[1]);

                if (playerData != null){

                    //Si el jugador es el que ha creado la sala, debera esperar hasta que su sala
                    // tenga a todos los participantes antes de obtener la informacion de los mismos
                    if(playerData[3].equalsIgnoreCase("anfitrion")){
                        while (!Server.getFullGame(playerData[4],dataReceived[0])){
                            wait50();
                        }
                    }

                    //Se obtiene la informacion de todos los participantes de la partida
                    String[] gameData = Server.getGameData(playerData,dataReceived[0]);
                    if (gameData != null){
                        // Envia de vuelta al jugador toda la informacion que necesita
                        // en formato CSV
                        pw.println(arrayToCSV(gameData));
                    }else {
                        pw.println("error");
                    }
                }else{
                    pw.println("error");
                }

            }else{//Si no lo tiene, significa que se trata del id de la partida que ha terminado
                Server.gameConcluded(Integer.parseInt(message));
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error al recibir peticiones");
        }
    }

    /**
     * Metodo que convierte un array en un String formato CSV
     * Usado para enviar arrays de informacion entre servidor y cliente
     * @param array array a convertir
     * @return String CSV
     */
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

    /**
     * Metodo que simplemente lanza un sleep de 50 milisegundos
     * Usado para que los anfitriones esperen hasta tener un invitado del que obtener su informacion
     */
    private void wait50() {
        try {
            sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}