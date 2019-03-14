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
import java.io.IOException;
import multicast.MulticastReceiver;
import interfaces.Game;

public class GameClient2 {

    public static void startGame() throws IOException, InterruptedException {
        String data = "";
        Thread t;
        MulticastReceiver receptor;
        receptor = new MulticastReceiver();
        t = new Thread(receptor);
        t.start();
        t.join();
        data = receptor.getData();
    }
    public static void startUI(){
        Login login = new Login();
        login.setVisible(true);
    }
    
    
    

    public static void main(String args[]) {
        //startRMI();
        startUI();
        
    }
}
