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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JGUTIERRGARC
 */
public class MulticastSenderPeer {

    public final String MULTICAST_GROUP = "228.5.6.7";
    public final int MULTICAST_PORT = 6789;
    public final int DELAY = 1000;
    public final int DELAY_ENTRE_JUEGOS = 3000;
    public final int MAX_X = 2;
    public final int MAX_Y = 2;
    public final int NUM_MONSTRUOS = 3;
    private int contadorMons = 0;

    public int getContadorMons() {
        return contadorMons;
    }

    public void setContadorMons(int contadorMons) {
        this.contadorMons = contadorMons;
    }

    public void juega() throws InterruptedException {
        while (true) {
            if (this.getContadorMons() >= this.NUM_MONSTRUOS) {
                System.out.println("Enviando mensaje: Fin del juego");
                enviarMensaje("Fin del juego");
                this.setContadorMons(0);
                Thread.sleep(DELAY_ENTRE_JUEGOS);

            } else {
                int posX = generarAleatorio(0, MAX_X);
                int posY = generarAleatorio(0, MAX_Y);
                String posicion = empaquetarPosicion(posX, posY);
                System.out.println("Enviando posición: " + posicion);
                enviarMensaje(posicion);
                Thread.sleep(DELAY);
                aumentarContadorMons();
            }
        }
    }

    public void aumentarContadorMons() {
        int contadorActual = this.getContadorMons();
        int nuevoContador = (contadorActual + 1); //% this.NUM_MONSTRUOS;
        this.setContadorMons(nuevoContador);
    }

    public int generarAleatorio(int min, int max) {
        Random rand = new Random();
        return min + rand.nextInt(max - min + 1);
    }

    public String empaquetarPosicion(int x, int y) {
        String posicion = "(" + x + "," + y + ")";
        return posicion;
    }

    public void enviarMensaje(String mensaje) {
        MulticastSocket s = null;
        try {

            InetAddress group = InetAddress.getByName(MULTICAST_GROUP); // destination multicast group 
            s = new MulticastSocket(MULTICAST_PORT);
            s.joinGroup(group);
            //s.setTimeToLive(10);
            //System.out.println("Messages' TTL (Time-To-Live): " + s.getTimeToLive());
            String myMessage = mensaje;
            byte[] m = myMessage.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);
            s.send(messageOut);
            s.leaveGroup(group);
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

    public static void main(String args[]) {
        MulticastSenderPeer sender = new MulticastSenderPeer();
        try {
            sender.juega();
        } catch (InterruptedException ex) {
            Logger.getLogger(MulticastSenderPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
