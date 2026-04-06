package org.example.server.commands;

import org.example.common.Request;
import org.example.common.Response;
import org.example.common.models.Organization;
import org.example.server.manager.CollectionManager;

import java.time.LocalDate;

public class Add extends Command{
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager){
        super("add");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            Organization org = request.getOrganization();
            if (org == null){
                return new Response(false, "Ошибка при передачи организации", null);
            }
            org.setId(collectionManager.getNextAvailableId());
            org.setCreationDate(LocalDate.now());
            collectionManager.addToCollection(org);

            return new Response(true, "Организация добавлена", null);
        } catch (Exception e){
            return new Response(false, "Ошибка при добавлении: " + e.getMessage(), null);
        }
    }
}
