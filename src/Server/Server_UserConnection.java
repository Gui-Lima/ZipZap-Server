package Server;

import Controllers.Server;
import Models.Message;
import Models.Type;
import Models.User;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Server_UserConnection implements Runnable {
    protected Socket clientSocket = null;
    protected String serverText = null;
    protected User connectedTo;
    protected Server_Run server;
    protected ArrayList<Message> messageList;

    private boolean stopped = true;

    public Server_UserConnection(Socket clientSocket, String serverText, Server_Run server) {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
        this.server = server;
        this.messageList = new ArrayList<>();
    }

    @Override
    public void run() {
        stopped = false;
        try {
            while (!stopped) {
                DataInputStream inData = new DataInputStream(clientSocket.getInputStream());
                String stringuiline = inData.readUTF();
                handleInput(stringuiline);
                DataOutputStream outData = new DataOutputStream(clientSocket.getOutputStream());
                outData.writeUTF(stringuiline);
            }
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleInput(String input) {
        Message message = new Message(input);
        if(message.getType() == Type.CONNECT){
            createUserConnection(message.getToPort());
        }
        else if(message.getType() == Type.MESSAGE){
            sendMessageToUser(message);
        }
        else if(message.getType() == Type.FINISH){
            endConnectionToUser();
        }
    }

    private void endConnectionToUser() {
        this.stopped = true;
    }

    private void createUserConnection(int port) {
        connectedTo = findUser(port);
        if(connectedTo.equals(null)){
            System.out.println("Não existe esse usuário");
            return;
        }
        System.out.println(connectedTo.getPort());
    }

    private User findUser(int port) {
        for (User user: server.getUserList()){
            if(user.getPort() == port){
                return user;
            }
        }
        return null;
    }

    private void sendMessageToUser(Message message){
        try {
            this.server.sendMessage(message, connectedTo);
        } catch (IOException e) {
            System.out.println("erro");
            e.printStackTrace();
        }
    }

}