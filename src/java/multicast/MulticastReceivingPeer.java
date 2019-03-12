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
public class MulticastReceivingPeer implements Runnable {

    public final String MULTICAST_GROUP = "228.5.6.7";
    public final int MULTICAST_PORT = 6789;
    private MulticastSocket socket;
    private volatile String data = "";

    public MulticastReceivingPeer() throws IOException {
        this.socket = crearSocket();

    }

    public String getData() {
        return data;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

    public MulticastSocket crearSocket() throws UnknownHostException, IOException {
        MulticastSocket s = null;
        InetAddress group = InetAddress.getByName(MULTICAST_GROUP); // destination multicast group 
        s = new MulticastSocket(MULTICAST_PORT);
        s.joinGroup(group);
        return s;
    }

    public void cerrarConexion() throws IOException {
        MulticastSocket socket = this.getSocket();
        InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
        socket.leaveGroup(group);
    }

    public void escuchar() throws InterruptedException {
        MulticastSocket s = null;
        try {
            s = this.crearSocket();

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

    public static void main(String args[]) throws IOException {
        Thread t = new Thread(new MulticastReceivingPeer());
        t.start();
    }

    @Override
    public void run() {
        try {
            this.escuchar();
            this.cerrarConexion();
        } catch (InterruptedException ex) {
            Logger.getLogger(MulticastReceivingPeer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MulticastReceivingPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
