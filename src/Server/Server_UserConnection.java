package Server;

import Models.Message;
import Models.Status;
import Models.Type;
import Models.User;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Server_UserConnection implements Runnable {
    protected Socket clientSocket;
    protected String serverText;
    private DataInputStream input;
    private DataOutputStream output;
    private User myUser;
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
                this.input = new DataInputStream(clientSocket.getInputStream());
                String message = input.readUTF();
                System.out.println("Received Message from: " + this.clientSocket.getPort());
                System.out.println("Message is: " + message);
                handleInput(message);
            }
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleInput(String input) {
        Message message = new Message(input);
        if(message.getType() == Type.STATUS_UPDATE){
            this.sendMessageStatusUpdateTo(message);
        }
        if(message.getType() == Type.CONNECT_TO || message.getType() == Type.RECEIVE_CONNECTION){
            System.out.println("Since it is a Connection message, i'm connecting this user to");
            createUserConnection(message.getToPort());
        }
        else if(message.getType() == Type.MESSAGE_SEND){
            System.out.println("Since it is a Message message, i'm sending it to: " + this.connectedTo.getPort());
            sendMessageToUser(message);
        }
        else if(message.getType() == Type.FINISH){
            endConnectionToUser();
        }
        else if(message.getType() == Type.MESSAGE_DELETE){
            System.out.println("I'm trying to delete a message");
            deleteMessageOnChat(message);
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

        Message message = new Message(Type.RECEIVE_CONNECTION, Status.CHECK_C, clientSocket.getPort(), port, "receiving connection");
        try {
            this.server.sendMessage(message, connectedTo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(connectedTo.getPort());
    }

    private User findUser(int port) {
        for (User user: server.getUserList()){  
            if(user.getPort() == port){
                return user;
            }
        }
        System.out.println("There's not user with this port: " + port);
        return null;
    }

    private void sendMessageToUser(Message message){
        try {
            message.setStatus(Status._);
            this.server.sendMessage(message, connectedTo);
            message.setStatus(Status.DOUBLE_CHECK_C);
            this.sendMessageStatusUpdate(message);
        } catch (IOException e) {
            System.out.println("erro");
            e.printStackTrace();
        }
    }

    private void sendMessageStatusUpdateTo(Message message){
        try{
            Message m = new Message(message);
            this.server.statusUpdate(m, this.connectedTo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageStatusUpdate(Message message){
        try{
            Message m = new Message(message);
            m.setType(Type.STATUS_UPDATE);
            this.myUser = this.findUser(this.clientSocket.getPort());
            this.server.statusUpdate(m, this.myUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMessageOnChat(Message message){
        try{
            this.server.deleteMessage(message, connectedTo);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}