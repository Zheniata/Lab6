package org.example.server.commands;

import org.example.common.Request;
import org.example.common.Response;
import org.example.server.manager.CollectionManager;

public class Show extends Command{
    CollectionManager collectionManager;

    public Show(CollectionManager collectionManager){
        super("add");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            String result = collectionManager.show();
            return new Response(true, "Элементы коллекции, отсортированные по годовому обороту", result);
        } catch (Exception e){
            return new Response(false, "Ошибка: " + e.getMessage(), null);
        }
    }
}
