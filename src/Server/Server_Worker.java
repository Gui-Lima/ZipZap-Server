package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server_Worker
        implements Runnable {
    protected Socket clientSocket = null;
    protected String serverText = null;

    public Server_Worker(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
    }

    @Override
    public void run() {
        try {
            InputStream input = this.clientSocket.getInputStream();
            OutputStream output = this.clientSocket.getOutputStream();
            long time = System.currentTimeMillis();
            output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: " + this.serverText + " - " + time + "").getBytes());
            this.readInputStream();
            output.close();
            input.close();
            System.out.println("Request processed: " + time + " from " + this.clientSocket.getPort());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readInputStream() {
        try {
            byte[] messageByte = new byte[1000];
            boolean end = false;
            String dataString = "";
            DataInputStream in = new DataInputStream(this.clientSocket.getInputStream());
            int bytesRead = 0;
            messageByte[0] = in.readByte();
            messageByte[1] = in.readByte();
            ByteBuffer byteBuffer = ByteBuffer.wrap(messageByte, 0, 2);
            short bytesToRead = byteBuffer.getShort();
            while (!end) {
                bytesRead = in.read(messageByte);
                if ((dataString = dataString + new String(messageByte, 0, bytesRead)).length() != bytesToRead) continue;
                end = true;
            }
            return dataString;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}