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
import entities.GameLogic;

/**
 *
 * @author planzagom
 */
public class TCPReceiver extends Thread  {

    String TCPHost;
    int TCPPort;
    GameLogic gameLogic;

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public TCPReceiver(String TCPHost, int TCPPort, GameLogic gameLogic) {
        this.TCPHost = TCPHost;
        this.TCPPort = TCPPort;
        this.gameLogic = gameLogic;

    }

    public void listen() {
        try {
            ServerSocket listenSocket = new ServerSocket(TCPPort);
            while (true) {
                System.out.println("Waiting for messages...");
                Socket clientSocket = listenSocket.accept();  // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made. 
                Connection c = new Connection(clientSocket, gameLogic);
                c.start();
            }
        } catch (IOException e) {
            System.out.println("Listen :" + e.getMessage());
        }
    }
    @Override
    public void run(){
        listen();
    }

    class Connection extends Thread {

        DataInputStream in;
        DataOutputStream out;
        Socket clientSocket;
        GameLogic gameLogic;

        public Connection(Socket aClientSocket, GameLogic gl) {
            try {
                clientSocket = aClientSocket;
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                gameLogic = gl;
            } catch (IOException e) {
                System.out.println("Connection:" + e.getMessage());
            }
        }

        public String[] unpackTCPMsg(String msg) {
            return msg.split("_");
        }

        @Override
        public void run() {
            try {			                 // an echo server
                String data = in.readUTF();
                System.out.println("Message received from: " + clientSocket.getRemoteSocketAddress() +" : "+ data);
                String[] params = unpackTCPMsg(data);
                String answer = answer(params[0], Integer.parseInt(params[1]));
                out.writeUTF(answer);
                if(answer.contains("f")){
                    this.gameLogic.clearScores();
                    this.gameLogic.scoreData.clear();
                }
                


            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO:" + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }

        public String answer(String username, int hitPosition) {
            
            String answer = "l";
            if (gameLogic.isWinner()) {
                answer = "l";
            } else {
                if (gameLogic.getCorrectButton() == hitPosition) {
                    answer = "w";
                    gameLogic.incScore(username);
                    gameLogic.setWinner(true);
                }
            }
            int newScore = gameLogic.scoreData.get(username)==null?0:gameLogic.scoreData.get(username);
            
            answer += "_" + newScore;
            String winnerUser = gameLogic.getWinnerUsername();
            if(winnerUser!=null){
                return "f_"+winnerUser;
            }
            return answer;
        }

    }

}
