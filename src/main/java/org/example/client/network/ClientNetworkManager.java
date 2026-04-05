package org.example.client.network;

import org.example.common.Request;
import org.example.common.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientNetworkManager {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private String host;
    private int port;

    public ClientNetworkManager(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Подключенно к серверу " + host + ": " + port);
        } catch (IOException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
        }
    }

    public Response sendRequest(Request request) throws IOException, ClassNotFoundException {
        out.writeObject(request);
        out.flush();
        Response response = (Response) in.readObject();
        return response;
    }

    public void disconnect() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();

        System.out.println("Отключено от сервера");
    }
}