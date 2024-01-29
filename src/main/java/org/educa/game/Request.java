package org.educa.game;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Request implements Runnable {
    Socket socket;

    public Request(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream is = socket.getInputStream();
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader bfr = new BufferedReader(isr);
             OutputStream os = socket.getOutputStream();
             PrintWriter pw = new PrintWriter(os)) {

            String message = bfr.readLine();

            if (message.split(",").length > 1) {
                String[] dataReceived = message.split(",");
                String[] playerData = Server.newPlayer(dataReceived[0], dataReceived[1]);

                if (playerData != null){
                    if(playerData[3].equalsIgnoreCase("anfitrion")){
                        while (!Server.getFullGame(playerData[4],dataReceived[0])){
                            wait50();
                        }
                    }
                    String[] gameData = Server.getGameData(playerData,dataReceived[0]);
                    if (gameData != null){
                        pw.println(arrayToCSV(gameData));
                    }else {
                        pw.println("error");
                    }
                }else{
                    pw.println("error");
                }

            }else{
                Server.gameConcluded(Integer.parseInt(message));
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

    private void wait50() {
        try {
            sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}