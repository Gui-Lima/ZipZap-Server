package Server;

import Models.Message;
import Models.User;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server_Run implements Runnable {
    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    protected ArrayList<User> userList;


    public Server_Run(int port ) {
        this.serverPort = port;
        this.userList = new ArrayList<>();
    }


    @Override
    public void run() {
        Server_Run server_Run = this;
        synchronized (server_Run) {
            this.runningThread = Thread.currentThread();
        }
        this.openServerSocket();
        while (!this.isStopped()) {
            Socket clientSocket = null;
            try {
                System.out.println("Waiting on port " + this.serverPort);
                clientSocket = this.serverSocket.accept();
            }
            catch (IOException e) {
                if (this.isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            Server_UserConnection connection = new Server_UserConnection(clientSocket, "Hi, I'm Connecting", this);
            User fulano = new User(clientSocket.getPort(), connection);
            this.userList.add(fulano);
            new Thread(connection).start();

        }
        System.out.println("Server Stopped.");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            for (User user : this.userList){
                user.getConnection().clientSocket.close();
            }
            this.serverSocket.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }
    public ArrayList<User> getUserList(){
        return this.userList;
    }

    public void sendMessage(Message message, User user) throws IOException {
        DataOutputStream data = new DataOutputStream(user.getConnection().clientSocket.getOutputStream());
        System.out.println("Sending " +  message.getText() + " to " + message.getToPort());
        data.writeUTF(message.toString());
    }

    public void deleteMessage(Message message, User user) throws IOException{
        DataOutputStream data = new DataOutputStream(user.getConnection().clientSocket.getOutputStream());
        System.out.println("Sending request do delete message " +message.toString());
        data.writeUTF(message.toString());
    }

    public void statusUpdate(Message message, User user) throws IOException{
        DataOutputStream data = new DataOutputStream(user.getConnection().clientSocket.getOutputStream());
        System.out.println("Sending status update of the messgae");
        data.writeUTF(message.toString());
    }
}