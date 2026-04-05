package org.example.server.manager;

import org.example.common.models.Organization;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class CollectionManager {
    private PriorityQueue<Organization> collection = new PriorityQueue<Organization>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final XmlFileManager fileManager;
    private final Set<Long> usedIds = new HashSet<>();

    public CollectionManager(XmlFileManager fileManager){
        this.fileManager = fileManager;
        loadCollection();
    }

    /**
     * Загружает коллекцию организаций из XML-файла.
     * Сохраняет оригинальные ID из файла и обновляет множество {@code usedIds}.
     * Если файл отсутствует или повреждён, коллекция остаётся пустой.
     */

    private void loadCollection(){
        try {
            List<Organization> loaded = fileManager.readCollection();
            usedIds.clear();
            for (Organization org : loaded) {
                long id = org.getId();
                usedIds.add(id);
            }
            collection.clear();
            collection.addAll(loaded);
            lastInitTime = LocalDateTime.now();
            System.out.println("Загружены данные из файла " + System.getenv("XML_FILENAME") + ". В коллекции " + loaded.size() + " объект(а/ов).");
        }
        catch (Exception e) {
            System.err.println("Ошибка при загрузке (или файл отсутсвует)");
            collection.clear();
            usedIds.clear();
        }
    }

    /**
     * Генерирует следующий доступный (минимальный положительный незанятый) идентификатор.
     * Добавляет его в множество {@code usedIds} и возвращает.
     *
     * @return новый уникальный ID
     */

    public long getNextAvailableId() {
        long id = 1;
        while (usedIds.contains(id)) {
            id++;
        }
        usedIds.add(id);
        return id;
    }

    /**
     * Сохраняет текущую коллекцию в XML-файл.
     * Обновляет время последнего сохранения.
     */

    public void saveCollection(){
        try {
            fileManager.writeCollection(new ArrayList<>(collection));
            lastSaveTime = LocalDateTime.now();
            System.out.println("Коллекция успешна сохранена");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении");
        }
    }

    /**
     * Возвращает организацию по её уникальному идентификатору.
     *
     * @param id идентификатор организации
     * @return найденная организация или {@code null}, если не найдена
     */

    public Organization getById(long id){
        for (Organization element: collection){
            if(element.getId() == id){
                return element;
            }
        }
        return null;
    }

    /**
     * Добавляет новую организацию в коллекцию.
     *
     * @param element организация для добавления
     */

    public void addToCollection(Organization element){
        collection.add(element);
    }

    /**
     * Удаляет указанную организацию из коллекции.
     *
     * @param element организация для удаления
     */

    public void removeFromCollection(Organization element){
        collection.remove(element);
    }

    /**
     * Проверяет, существует ли организация с заданным ID.
     *
     * @param id идентификатор для проверки
     * @return {@code true}, если организация существует; иначе {@code false}
     */

    public boolean checkExist(long id){
        for (Organization element: collection){
            if (element.getId() == id){
                return true;
            }
        }
        return false;
    }

    /**
     * Полностью очищает коллекцию.
     */

    public void clearCollection(){
        collection.clear();
    }

    /**
     * Возвращает размер коллекции.
     *
     * @return количество организаций в коллекции
     */

    public int size() {
        return collection.size();
    }

    /**
     * Проверяет, пуста ли коллекция.
     *
     * @return {@code true}, если коллекция пуста; иначе {@code false}
     */

    public boolean isEmpty() {
        return collection.isEmpty();
    }

    /**
     * Возвращает саму коллекцию организаций.
     *
     * @return коллекцию
     */

    public PriorityQueue<Organization> getCollection() {
        return collection;
    }

    /**
     * Возвращает время последней загрузки данных.
     *
     * @return дата и время инициализации или {@code null}, если ещё не загружалась
     */

    public LocalDateTime getTime(){
        return lastInitTime;
    }

    /**
     * Возвращает время последнего сохранения данных.
     *
     * @return дата и время последнего сохранения или {@code null}, если ещё не сохранялась
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }
}

