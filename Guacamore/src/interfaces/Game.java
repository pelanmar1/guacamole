package interfaces;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JGUTIERRGARC
 */
import entities.ConnectionInfo;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Game extends Remote {
	
	public boolean registerPlayer(String nombe) throws RemoteException;
	public ConnectionInfo getConnectionInfo() throws RemoteException;
        

	
}

