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
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import multicast.MulticastSender;
import multicast.MulticastReceiver;
import interfaces.Game;
import java.util.Hashtable;

public class GameServer implements Game {
    public static int RMI_PORT = 1099;
    
    public static String UDP_HOST = "228.11.13.17";
    public static String TCP_HOST = "localhost";
    public static int UDP_PORT = 1100;
    public static int TCP_PORT = 1101;
    
    Hashtable<String, Integer> playersData = new Hashtable<String, Integer>();
    

    public GameServer() throws RemoteException {
        super();

    }
    
    public static void startRMI(){
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

    public static void main(String[] args) throws RemoteException {
        startRMI();
        
        // Start multicast
        MulticastSender multicastSender = new MulticastSender(UDP_HOST,UDP_PORT);
        multicastSender.start();
        
        
    }

    @Override
    public boolean registerPlayer(String id) throws RemoteException {
        boolean status = false;
        if (playersData.get(id) == null && !playersData.containsKey(id)) {
            int score = 0;
            playersData.put(id, score);
            status = true;
        }
        return status;
    }

    @Override
    public ConnectionInfo getConnectionInfo() throws RemoteException {
        ConnectionInfo ci = new ConnectionInfo(UDP_HOST,UDP_PORT,TCP_HOST,TCP_PORT);
        return ci;
    }

    

}
