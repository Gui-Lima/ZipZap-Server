package Models;

import Server.Server_UserConnection;

public class User {
    private String username;
    private int port;
    private Server_UserConnection connection;

    public User(int port, Server_UserConnection connectionThread){
        this.port = port;
        this.connection = connectionThread;
    }

    public User(){};

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Server_UserConnection getConnection() {
        return connection;
    }

    public void setConnection(Server_UserConnection connection) {
        this.connection = connection;
    }
}
