/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author PLANZAGOM
 */
public class ConnectionInfo implements Serializable {

    private String UDPHost;
    private int UDPPort;
    private String TCPHost;
    private int TCPPort;

    public ConnectionInfo(String UDPHost, int UDPPort, String TCPHost, int TCPPort) {
        this.UDPHost = UDPHost;
        this.UDPPort = UDPPort;
        this.TCPHost = TCPHost;
        this.TCPPort = TCPPort;
    }

    public String getUDPHost() {
        return UDPHost;
    }

    public int getUDPPort() {
        return UDPPort;
    }

    public String getTCPHost() {
        return TCPHost;
    }

    public int getTCPPort() {
        return TCPPort;
    }

    @Override
    public String toString() {
        return UDPHost + "_" + UDPPort + "_" + TCPHost + "_" + TCPPort;
    }

}
