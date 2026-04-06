package org.example.client.network;

import org.example.common.Request;
import org.example.common.Response;
import org.example.common.util.SerializationUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientNetworkManager {
    private SocketChannel channel;
    private static final int BUFFER_SIZE = 8192;


    public ClientNetworkManager(String host, int port) throws IOException {
        channel = SocketChannel.open();
        channel.connect(new java.net.InetSocketAddress(host, port));
        System.out.println("Подключено к серверу " + host + ":" + port);
    }

    public Response sendRequest(Request request) throws IOException, ClassNotFoundException {
        ByteBuffer requestBuffer = SerializationUtil.serialize(request);
        while (requestBuffer.hasRemaining()) {
            channel.write(requestBuffer);
        }

        ByteBuffer responseBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        channel.read(responseBuffer);
        responseBuffer.flip();

        return (Response) SerializationUtil.deserialize(responseBuffer);
    }

    public void disconnect() throws IOException {
        if (channel != null) channel.close();
        System.out.println("Отключено от сервера");
    }
}