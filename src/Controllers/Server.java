package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import Server.Server_Run;

public class Server {
    @FXML
    Button startStopButton;

    Server_Run server;

    public void handleStartStopButton(){

        if (startStopButton.getText().equals("Turn On")){
            startServer();
            //Loading Screen
            startStopButton.setText("Turn Off");
        }
        else if(startStopButton.getText().equals(("Turn Off"))){
            stopServer();
            //Loading Screen
            startStopButton.setText(("Turn On"));
        }

    }

    private void startServer() {
        Server_Run server = new Server_Run(9000);
        new Thread(server).start();

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void stopServer(){
        if (server != null) server.stop();
    }

}
