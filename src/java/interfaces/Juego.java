package interfaces;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JGUTIERRGARC
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Juego extends Remote {
	// Calculate the square of a number
	public boolean registrarJugador(String nombe) throws RemoteException;
	// Calculate the power of a number
	//public double power ( int num1, int num2) throws RemoteException;
        public String getPosicion() throws RemoteException;
}

