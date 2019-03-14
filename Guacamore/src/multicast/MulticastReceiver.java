/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast;

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

    public final String MULTICAST_GROUP = "228.5.6.7";
    public final int MULTICAST_PORT = 6789;
    private MulticastSocket socket;
    private volatile String data = "";

    public MulticastReceiver() throws IOException {
        this.socket = createSocket();

    }

    public String getData() {
        return data;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

    public MulticastSocket createSocket() throws UnknownHostException, IOException {
        MulticastSocket s = null;
        InetAddress group = InetAddress.getByName(MULTICAST_GROUP); // destination multicast group 
        s = new MulticastSocket(MULTICAST_PORT);
        s.joinGroup(group);
        return s;
    }

    public void closeConnection() throws IOException {
        MulticastSocket socket = this.getSocket();
        InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
        socket.leaveGroup(group);
    }

    public void listen() throws InterruptedException {
        MulticastSocket s = null;
        try {
            s = this.createSocket();

            byte[] buffer = new byte[1000];
            //while (true) {
            
            System.out.println("Waiting for messages");
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            s.receive(messageIn);
            System.out.println("Message from: " + messageIn.getAddress());
            String msg = new String(messageIn.getData()).trim();
            this.data = msg + " bebe";
            System.out.println(data);
            Thread.sleep(2000);


            //}
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
    
    public static void main(String args[]) throws IOException {
        MulticastReceiver t = new MulticastReceiver();
        t.start();
    }
}
