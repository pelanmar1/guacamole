/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast;

import entities.ConnectionInfo;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JGUTIERRGARC
 */
public class MulticastReceiver extends Thread {

    public String multicastGroup;
    public int multicastPort;
    private MulticastSocket socket;
    private volatile String data = "";

    public MulticastReceiver(ConnectionInfo ci) throws IOException {
        this.multicastGroup = ci.getUDPHost();
        this.multicastPort = ci.getUDPPort();
    }

    public String getData() {
        return data;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

    public MulticastSocket createSocket() throws UnknownHostException, IOException {
        MulticastSocket s = null;
        InetAddress group = InetAddress.getByName(multicastGroup); // destination multicast group 
        s = new MulticastSocket(multicastPort);
        s.joinGroup(group);
        return s;
    }

    public void closeConnection() throws IOException {
        MulticastSocket socket = this.getSocket();
        if (socket!=null) {
            InetAddress group = InetAddress.getByName(multicastGroup);
            socket.leaveGroup(group);
            socket.close();
        }

    }

    public void listen() throws InterruptedException {
        MulticastSocket s = null;
        try {
            s = this.createSocket();

            byte[] buffer = new byte[1000];
            System.out.println("Waiting for messages from " + multicastGroup + ":" + multicastPort);
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            s.receive(messageIn);
            System.out.println("Message from: " + messageIn.getAddress());
            String msg = new String(messageIn.getData()).trim();
            this.data = msg;
            System.out.println(data);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    @Override
    public void run() {
        try {
            this.listen();
            this.closeConnection();
        } catch (InterruptedException ex) {
            Logger.getLogger(MulticastReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MulticastReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
