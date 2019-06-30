package Controllers;

import Models.User;
import Server.Server_Run;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Server {
    @FXML
    Button startStopButton;
    @FXML
    ListView<User> usersList;

    Server_Run server;

    private List<User> usuarios = new ArrayList<>();
    //    private List<String> infor = new ArrayList<>();
    private ObservableList<User> obsUsuarios;

    //    private ObservableList<String> obsInfo;
    public void handleStartStopButton() {
        if (this.startStopButton.getText().equals("Turn On")) {
            this.startServer();
            this.startStopButton.setText("Turn Off");
        } else if (this.startStopButton.getText().equals("Turn Off")) {
            this.stopServer();
            this.startStopButton.setText("Turn On");
        }

    }

    private void startServer() {
        this.server = new Server_Run(9000);
        new Thread((Runnable) this.server).start();
    }

    private void stopServer() {
        if (!this.server.equals(null)) {
            this.server.stop();
        }
    }

    public void carregarUsuarios() {

         if (this.startStopButton.getText().equals("Turn Off")) {

             usuarios = server.getUserList();

//            User u1 = new User(192, null);
//            User u2 = new User(192, null);
//
//            usuarios.add(u1);
//            usuarios.add(u2);

            obsUsuarios = FXCollections.observableArrayList(usuarios);
            usersList.setItems(obsUsuarios);
//            usuarios.remove(1);
//            usuarios.remove(0);
        }
    }

}


