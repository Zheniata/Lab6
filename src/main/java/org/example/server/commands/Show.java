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
            int size = collectionManager.size();
            if (result == null || result.isEmpty()){
                return new Response(true, "Коллекция пуста", null);
            }
            return new Response(true, "В коллекции " + size + " элемент/ов, отсортированный по годовому обороту", result);
        } catch (Exception e){
            return new Response(false, "Ошибка: " + e.getMessage(), null);
        }
    }
}
