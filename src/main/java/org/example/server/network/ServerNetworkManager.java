package org.example.server.network;

import org.example.common.Request;
import org.example.common.Response;
import org.example.server.handlers.RequestHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetworkManager {
    private ServerSocket serverSocket;
    private int port;

    public ServerNetworkManager(int port) throws IOException{
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        System.out.println("Сервер создан на порту " + port);
    }

    public void start(RequestHandler handler){
        System.out.println("Сервер запущен");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новый клиент: " + clientSocket.getInetAddress());
                handleClient(clientSocket, handler);
            } catch (IOException e){
                System.err.println("Ошибка при подключения: " + e.getMessage());
            }
        }
    }

    private void handleClient(Socket clientSocket, RequestHandler requestHandler){
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            Request request = (Request) in.readObject();
            System.out.println("Получен запрос: " + request.getName());

            Response response = requestHandler.handle(request);

            out.writeObject(response);
            out.flush();
            System.out.println("Ответ отправлен: " + response.getMessage());

        } catch (IOException e) {
            System.out.println("Ошибка сети: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Неизвестный класс: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Клиент отключен");
            } catch (IOException e) {

            }
        }
    }

    public void close(){
        try {
            if (serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
                System.out.println("Сервер закрыт");
            }
        } catch (IOException e){
            System.out.println("Ошибка при закрытие сервера: " + e.getMessage());
        }
    }
}
