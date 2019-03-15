/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stress;

import client.GameClient;
import entities.ConnectionInfo;
import entities.TimeWatch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import multicast.MulticastReceiver;
import server.GameServer;
import tcp.TCPSender;

/**
 *
 * @author PLANZAGOM
 */
public class Stressinator extends Thread {

    MulticastReceiver mr;
    ConnectionInfo ci;
    String username;
    TimeWatch watch;
    int numMsgs;
    int numClients;

    public Stressinator(String username, int numMsgs, int numClients) throws IOException {
        String UDP_HOST = "228.11.13.17";
        String TCP_HOST = "localhost";
        int UDP_PORT = 1100;
        int TCP_PORT = 1101;
        ci = new ConnectionInfo(UDP_HOST, UDP_PORT, TCP_HOST, TCP_PORT);
        mr = new MulticastReceiver(ci);
        this.username = username;
        this.numMsgs = numMsgs;
        this.numClients = numClients;
    }

    public void sendPositionTCP(int position) throws InterruptedException, IOException {
        TCPSender ts = new TCPSender(ci.getTCPHost(), ci.getTCPPort(), username, position);
        ts.start();
        ts.join();
        String answer = ts.getLastAnswer();
        long passedTimeInSeconds = watch.time();
        String text = username + "," + passedTimeInSeconds;
        String file = "C:\\Users\\PLANZAGOM\\Desktop\\pedro\\guacamole\\Guacamore\\src\\stress\\datos2_" + this.numClients + ".csv";
        write(file, text);
    }

    public void write(String file, String text) throws IOException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            out.println(text);
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void listenAndUpdate() throws IOException, InterruptedException {
        (new Thread() {
            public void run() {
                for (int i = 0; i < numMsgs; i++) {
                    try {
                        mr = new MulticastReceiver(ci);
                        watch = TimeWatch.start();
                        mr.start();
                        mr.join();
                        String msj = mr.getData();
                        String[] msjArre = msj.split("_");
                        int position = Integer.valueOf(msjArre[0]);
                        sendPositionTCP(position);
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();

    }

    @Override
    public void run() {
        try {
            this.listenAndUpdate();
        } catch (IOException ex) {
            Logger.getLogger(Stressinator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Stressinator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws RemoteException, InterruptedException, IOException {
        System.setProperty("sun.net.maxDatagramSockets", "5000");
        // Create Server
        GameServer gameServer = new GameServer();
        gameServer.play();

        int numSamples = 15;
        int numClients = 50;
        int numMsgs = 10;

        int s = 15;
        //for (int s = 1; s <= numSamples; s++) {
        for (int i = 0; i < numClients * s; i++) {
            String username = "user_" + i;
            Stressinator stress = new Stressinator(username, numMsgs, numClients * s);
            stress.start();

        }
        //}

    }

}
