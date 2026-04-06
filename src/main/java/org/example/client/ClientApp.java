package org.example.client;

import org.example.client.network.ClientNetworkManager;
import org.example.client.util.Runner;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {
    private static final String host = "localhost";
    private static final int port = 12345;

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        try {
            ClientNetworkManager network = new ClientNetworkManager(host, port);

            Runner runner = new Runner(scanner, network);
            runner.interactiveMode();

            network.disconnect();
        } catch (IOException e){
            System.err.println("Ошибка подключения: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }
}
