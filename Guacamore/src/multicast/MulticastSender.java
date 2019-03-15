/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast;

import entities.GameLogic;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import entities.Position;

/**
 *
 * @author JGUTIERRGARC
 */
public class MulticastSender extends Thread {

    public String multicastHost;
    public int multicastPort;

    public final int DELAY = 1000;

    GameLogic gameLogic;

    public MulticastSender(String multicastHost, int multicastPort, GameLogic gameLogic) {
        this.multicastHost = multicastHost;
        this.multicastPort = multicastPort;
        this.gameLogic = gameLogic;
    }

    public void play() throws InterruptedException {
        while (true) {
            // Create random position
            int position = gameLogic.getRandomButton();
            String posStr = String.valueOf(position);
            //System.out.println("Enviando posición: " + posStr + " desde " + multicastHost + ":" + multicastPort );

            sendMessage(posStr);
            Thread.sleep(DELAY);
            gameLogic.setWinner(false);

        }
    }

    public void sendMessage(String mensaje) {
        MulticastSocket s = null;
        try {

            InetAddress group = InetAddress.getByName(multicastHost); // destination multicast group 
            s = new MulticastSocket(multicastPort);
            s.joinGroup(group);
            //s.setTimeToLive(10);
            //System.out.println("Messages' TTL (Time-To-Live): " + s.getTimeToLive());
            String winner = gameLogic.getWinnerUsername();
            if (winner != null) {
                this.gameLogic.clearScores();
                this.gameLogic.scoreData.clear();
                mensaje += "_" + winner;
            }
            String myMessage = mensaje;// + "_" + gameLogic.scoreData.toString();
            byte[] m = myMessage.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, multicastPort);
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

    @Override
    public void run() {
        try {
            play();
        } catch (InterruptedException ex) {
            Logger.getLogger(MulticastSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
