//
// indicate the location of security policies.
//

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.math.BigDecimal;
import interfaces.Juego;

public class JuegoClient {
    public static void main(String args[]) {
        System.setProperty("java.security.policy","file:\\C:\\Users\\PLANZAGOM\\Documents\\NetBeansProjects\\ProyectoAlpha\\src\\java\\client\\client.policy");
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Juego";
            Registry registry = LocateRegistry.getRegistry("localhost"); // server's ip address args[0]
            Juego juego = (Juego) registry.lookup(name);
            
            System.out.println("Registro "+juego.registrarJugador("Pedro"));
            
            
        } catch (Exception e) {
            System.err.println("exception");
            e.printStackTrace();
        }
    }    
}
