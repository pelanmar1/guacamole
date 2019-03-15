//
//
// indicate the location of security policies.
//
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import entities.ConnectionInfo;
import entities.GameLogic;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import multicast.MulticastSender;
import multicast.MulticastReceiver;
import interfaces.Game;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import tcp.TCPReceiver;

public class GameServer implements Game {

    public int RMI_PORT = 1099;

    public String UDP_HOST = "228.11.13.17";
    public String TCP_HOST = "localhost";
    public int UDP_PORT = 1100;
    public int TCP_PORT = 1101;
    private MulticastSender multicastSender;
    private TCPReceiver tcpReceiver;
    GameLogic gameLogic;

    public GameServer() throws RemoteException {
        super();
        gameLogic = new GameLogic();
    }

    public void startTCPReceiver() throws InterruptedException {
        tcpReceiver = new TCPReceiver(TCP_HOST, TCP_PORT, gameLogic);
        tcpReceiver.start();

    }

    public void startRMI() {
        String path = "C:\\Users\\PLANZAGOM\\Desktop\\pedro\\guacamole\\Guacamore\\src\\server\\server.policy";
        System.setProperty("java.security.policy", path);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {

            LocateRegistry.createRegistry(RMI_PORT);

            String name = "Game";
            GameServer engine = new GameServer();

            Game stub = (Game) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);

            System.out.println("JuegoEngine bound");
        } catch (Exception e) {
            System.err.println("JuegoEngine exception:");
            e.printStackTrace();
        }
    }

    public void startMulticastSender() {
        multicastSender = new MulticastSender(UDP_HOST, UDP_PORT, gameLogic);
        multicastSender.start();
    }
    
    public void play() throws InterruptedException{
        // Start RMI
        startRMI();
        // Start Multicast sender
        startMulticastSender();
        // Start TCPReceiver
        startTCPReceiver();
    }

    public static void main(String[] args) throws RemoteException, InterruptedException {
        GameServer gameServer = new GameServer();
        gameServer.play();

    }

    @Override
    public ConnectionInfo getConnectionInfo() throws RemoteException {
        ConnectionInfo ci = new ConnectionInfo(UDP_HOST, UDP_PORT, TCP_HOST, TCP_PORT);
        return ci;
    }

    @Override
    public boolean registerPlayer(String username) throws RemoteException {

        return gameLogic.insert(username);
    }

}
