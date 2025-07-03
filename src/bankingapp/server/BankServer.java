package bankingapp.server;

import java.net.*;
import java.io.*;

public class BankServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Bank Server is running...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("ATM connected: " + clientSocket.getInetAddress());

            // Spawn a new thread for handling ATM communication
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }
}
