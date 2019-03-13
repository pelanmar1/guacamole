//
//
// indicate the location of security policies.
//
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import interfaces.Juego;
import multicast.MulticastSenderPeer;
import multicast.MulticastReceivingPeer;

public class JuegoServer implements Juego {

    private int MAX_JUGADORES = 2;
    private Jugador jugadores[];
    private int numJugadores = 0;
    private volatile String posicion = "";

    public JuegoServer() throws RemoteException {
        super();
        this.jugadores = new Jugador[this.MAX_JUGADORES];

    }

    public static void main(String[] args) {
        String path = "C:\\Users\\PLANZAGOM\\Documents\\NetBeansProjects\\Guacamore\\src\\server\\server.policy";
        System.setProperty("java.security.policy", path);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {

            // start the rmiregistry 
            LocateRegistry.createRegistry(1099);   /// default port

            // if the rmiregistry is not started by using java code then
            // 1) Start it as follows: rmiregistry -J-classpath -J"c:/Users/jgutierrgarc/Documents/NetBeansProjects/JavaRMI/dist/javaRMI.jar" or 
            // 2) Add this to the classpath C:\Users\jgutierrgarc\Documents\NetBeansProjects\JavaRMI\dist\javaRMI.jar and then start the rmiregistry 
            String name = "Juego";
            JuegoServer engine = new JuegoServer();
            Juego stub
                    = (Juego) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);

            System.out.println("JuegoEngine bound");
        } catch (Exception e) {
            System.err.println("JuegoEngine exception:");
            e.printStackTrace();
        }
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    @Override
    public boolean registrarJugador(String nombre) throws RemoteException {
        boolean status = false;
        if (this.numJugadores < this.MAX_JUGADORES) {
            int idJugador = this.numJugadores;
            int scoreInicial = 0;
            Jugador jugador = new Jugador(nombre, idJugador, scoreInicial);
            jugadores[idJugador] = jugador;
            this.numJugadores += 1;
            status = true;
        }
        return status;
    }

}
