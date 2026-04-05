package org.example.server.network;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerNetworkManager {
    private ServerSocket serverSocket;
    private int port;

    public ServerNetworkManager(int port) throws IOException{
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        System.out.println("Сервер запущен на порту " + port);
    }
}
