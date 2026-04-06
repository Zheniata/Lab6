package org.example.server;

import org.example.server.commands.Add;
import org.example.server.commands.Show;
import org.example.server.handlers.RequestHandler;
import org.example.server.manager.CollectionManager;
import org.example.server.manager.CommandManager;
import org.example.server.manager.XmlFileManager;
import org.example.server.network.ServerNetworkManager;

import java.io.IOException;

public class ServerApp {
    private static final int port = 12345;

    public static void main(String[] args){
        String filename = System.getenv("XML_FILENAME");
        XmlFileManager xmlFileManager = new XmlFileManager(filename);
        CollectionManager collectionManager = new CollectionManager(xmlFileManager);

        CommandManager commandManager = new CommandManager(){{
            register("add", new Add(collectionManager));
            register("show", new Show(collectionManager));
        }};

        RequestHandler handler = new RequestHandler(commandManager);

        Runtime.getRuntime().addShutdownHook(new Thread(collectionManager::saveCollection));

        try {
            ServerNetworkManager server = new ServerNetworkManager(port);
            server.start(handler);
        } catch (Exception e) {
            System.out.println("Ошибка запуска сервера: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
