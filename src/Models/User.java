package Models;

import Controllers.Server;
import Server.Server_Worker;

public class User {
    private String username;
    private String address;
    private int port;
    private Server_Worker connection;

    public User(int port, Server_Worker connectionThread){
        this.port = port;
        this.connection = connectionThread;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Server_Worker getConnection() {
        return connection;
    }

    public void setConnection(Server_Worker connection) {
        this.connection = connection;
    }
}
