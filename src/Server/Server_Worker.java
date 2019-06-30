package Server;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server_Worker implements Runnable {
    protected Socket clientSocket = null;
    protected String serverText = null;
    private boolean stopped = true;

    public Server_Worker(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
    }

    @Override
    public void run() {
        stopped = false;
        try {
            while (!stopped) {
                DataInputStream inData = new DataInputStream(clientSocket.getInputStream());
                String stringuiline = inData.readUTF();
                System.out.println("yay, recebemos essa msg: " + stringuiline);
                if(stringuiline.equals("/exit")){
                    stopped = true;
                }
            }
            System.out.println("yay again, enviamos uma resposta pro cliente!");
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}