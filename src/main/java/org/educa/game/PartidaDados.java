package org.educa.game;

public class PartidaDados {

    private boolean full;
    private int port;
    private String P1_nickName;
    private String P1_host;
    private String P2_nickName;
    private String P2_host;

    public PartidaDados(String p1_nickName, int port) {
        this.full = false;
        this.port = port;
        P1_nickName = p1_nickName;
    }

    public void setP2(String p2_nickName, String p2_host){
        this.P2_nickName = p2_nickName;
        this.P2_host = p2_host;
        this.full = true;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getP1_nickName() {
        return P1_nickName;
    }

    public void setP1_nickName(String p1_nickName) {
        P1_nickName = p1_nickName;
    }

    public String getP1_host() {
        return P1_host;
    }

    public void setP1_host(String p1_host) {
        P1_host = p1_host;
    }

    public String getP2_nickName() {
        return P2_nickName;
    }



    public String getP2_host() {
        return P2_host;
    }



}
