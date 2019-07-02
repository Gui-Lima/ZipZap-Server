package Controllers;

import Models.User;
import Server.Server_Run;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.util.List;

public class Server {
    @FXML
    Button startStopButton;
    @FXML
    ListView<User> usersList;

    Server_Run server;

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
            this.server.stop();
    }

    public void carregarUsuarios() {
        List<User> usuarios = server.getUserList();
        ObservableList<User> obsUsuarios = FXCollections.observableArrayList(usuarios);
        usersList.setItems(obsUsuarios);
    }

}


