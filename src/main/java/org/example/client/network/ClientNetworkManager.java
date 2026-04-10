package org.example.client.network;

import org.example.common.Request;
import org.example.common.Response;
import org.example.common.util.SerializationUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Менеджер сетевого взаимодействия для клиента.
 * Отвечает за подключение к серверу, отправку запросов и получение ответов.
 * Использует {@link SocketChannel} для неблокирующего сетевого взаимодействия.
 * Поддерживает автоматические повторные попытки подключения при недоступности сервера.
 * @see Request
 * @see Response
 * @see SocketChannel
 */

public class ClientNetworkManager {
    private SocketChannel channel;
    private static final int BUFFER_SIZE = 8192;
    private final String host;
    private final int port;


    public ClientNetworkManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Устанавливает соединение с сервером с автоматическими повторными попытками.
     * При неудачной попытке подключения выводит сообщение об ошибке
     * и повторяет попытку через 3 секунды. Продолжает попытки до успешного
     * подключения или прерывания потока.
     *
     * @throws RuntimeException если поток прерван во время ожидания подключения
     */

    public void connect() {
        System.out.println("Подключение к серверу " + host + ":" + port + "...");

        while (true) {
            try {
                channel = SocketChannel.open();
                channel.configureBlocking(true);
                channel.connect(new InetSocketAddress(host, port));

                System.out.println("Подключено к серверу " + host + ":" + port);
                return;

            } catch (IOException e) {
                System.out.println("Сервер временно не доступен");
                System.out.println("Повторная попытка через 3 секунды");
            }
            try {
                Thread.sleep(3000);  // Ждём 3 секунды
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.out.println("Прервано");
                return;
            }
        }
    }

    /**
     * Отправляет запрос на сервер и возвращает ответ.
     * Сериализует объект {@link Request} в байтовый буфер, отправляет его
     * через сетевой канал, затем читает и десериализует ответ {@link Response}.
     *
     * @param request объект запроса, содержащий команду, аргументы и данные
     * @return объект ответа от сервера с результатом выполнения команды
     * @throws IOException если произошла ошибка при чтении/записи в канал
     * @throws ClassNotFoundException если не удалось десериализовать ответ
     */

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

    /**
     * Закрывает соединение с сервером и освобождает ресурсы.
     * Корректно закрывает сетевой канал, если он был открыт.
     * Рекомендуется вызывать после завершения работы клиента.
     *
     * @throws IOException если произошла ошибка при закрытии канала
     */

    public void disconnect() throws IOException {
        if (channel != null) channel.close();
        System.out.println("Отключено от сервера");
    }


}