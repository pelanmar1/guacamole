//
// indicate the location of security policies.
//

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import guacamore.Tablero;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.math.BigDecimal;
import interfaces.Juego;
import java.io.IOException;
import multicast.MulticastReceivingPeer;

public class JuegoClient {

    public static void iniciarJuego() throws IOException, InterruptedException {
        String data = "";
        Thread t;
        MulticastReceivingPeer receptor;
        receptor = new MulticastReceivingPeer();
        t = new Thread(receptor);
        t.start();
        t.join();
        data = receptor.getData();
    }
    public static void iniciarInterfaz(){
        Tablero tablero = new Tablero();
        tablero.setVisible(true);
    }

    public static void main(String args[]) {
        String path = "file:\\C:\\Users\\PLANZAGOM\\Documents\\NetBeansProjects\\Guacamore\\src\\client\\client.policy";
        System.setProperty("java.security.policy", path);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Juego";
            Registry registry = LocateRegistry.getRegistry("localhost"); // server's ip address args[0]
            Juego juego = (Juego) registry.lookup(name);

            boolean registro = juego.registrarJugador("Pedro");
            
            if(registro){
                iniciarInterfaz();
            }

        } catch (Exception e) {
            System.err.println("exception");
            e.printStackTrace();
        }
    }
}
