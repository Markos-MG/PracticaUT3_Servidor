package org.educa.game;

public class Partida {

    private boolean full;
    private String P1_nickName;
    private String P1_host;
    private String P1_port;
    private String P2_nickName;
    private String P2_host;
    private String P2_port;

    public Partida(String p1_nickName) {
        this.full = false;
        P1_nickName = p1_nickName;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
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

    public String getP1_port() {
        return P1_port;
    }

    public void setP1_port(String p1_port) {
        P1_port = p1_port;
    }

    public String getP2_nickName() {
        return P2_nickName;
    }

    public void setP2_nickName(String p2_nickName) {
        P2_nickName = p2_nickName;
        this.full = true;
    }

    public String getP2_host() {
        return P2_host;
    }

    public void setP2_host(String p2_host) {
        P2_host = p2_host;
    }

    public String getP2_port() {
        return P2_port;
    }

    public void setP2_port(String p2_port) {
        P2_port = p2_port;
    }
}
