/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author planzagom
 */
public class TCPSender extends Thread {

    String TCPHost;
    int TCPPort;
    String username;
    volatile String lastAnswer = "";
    int position;

    public TCPSender(String TCPHost, int TCPPort, String username, int position) {
        this.TCPHost = TCPHost;
        this.TCPPort = TCPPort;
        this.username = username;
        this.position = position;

    }

    public String getLastAnswer() {
        return lastAnswer;
    }
    

    public String packTCPMsg() {
        return username + "_" + position;
    }

    public void send() {
        Socket s = null;
        try {
            s = new Socket("localhost", TCPPort);
            //   s = new Socket("127.0.0.1", serverPort);    
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out
                    = new DataOutputStream(s.getOutputStream());
            String packedMsg = packTCPMsg();
            
            System.out.println("Sending message: " +packedMsg);
            out.writeUTF(packedMsg);        	// UTF is a string encoding 

            lastAnswer = in.readUTF();

            System.out.println("Received: " + lastAnswer);
        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("close:" + e.getMessage());
                }
            }
        }
    }

    @Override
    public void run() {
        send();
    }
}
