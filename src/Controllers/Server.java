package Controllers;

import Server.Server_Run;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Server {
    @FXML
    Button startStopButton;
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
        new Thread((Runnable)this.server).start();
    }

    private void stopServer() {
        if (!this.server.equals(null)) {
            this.server.stop();
        }
    }
}