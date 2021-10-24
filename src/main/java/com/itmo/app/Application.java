package com.itmo.app;

import com.itmo.data.City;
import com.itmo.data.Coordinates;
import com.itmo.data.Human;
import com.itmo.exceptions.InputFormatException;
import com.itmo.exceptions.SameIdException;
import com.itmo.utils.DatabaseManager;
import com.itmo.utils.FieldsValidator;
import lombok.Getter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * класс приложения, в котором соединены функции отправителя команды и получателя результата
 */
@Getter
public class Application {
    private ConcurrentHashMap<Long, City> collection;
    private HashSet<Long> idList;
    private LocalDateTime initializationDate;
    private DatabaseManager databaseManager;
    /**
     * создаём множество идентификаторов и устнавливаем время инициализации коллекции
     */
    public Application() throws SQLException {

        idList = new HashSet<>();
        databaseManager = new DatabaseManager();
        collection = databaseManager.getCollectionFromDatabase();
        if (collection != null) {
            for (City city : collection.values()) {
                try {
                    //проверка валидности данных
                    if (!FieldsValidator.checkNumber((long) city.getName().length(), 2, 19,
                            "У элемента некорректное имя, id: " + city.getId(), false)

                            || !FieldsValidator.checkNumber(city.getArea(), 1, Long.MAX_VALUE,
                            "У элемента некорректная площадь, id: " + city.getId(), false)

                            || !FieldsValidator.checkNumber((long) city.getPopulation(), 0, 300,
                            "У элемента некорректное число населения, id: " + city.getId(), false)

                            || !FieldsValidator.checkNumber(city.getId(), 0, Long.MAX_VALUE,
                            "У элемента некорректный id, имя элемента: " + city.getName(), false)

                            || city.getCoordinates() == null

                            || !FieldsValidator.checkNumber((long) city.getCoordinates().getX(), Coordinates.MIN_X, Coordinates.MAX_X,
                            "У элемента некорректная координата, id: " + city.getId(), false)

                            || !FieldsValidator.checkNumber((long) city.getCoordinates().getY(), Coordinates.MIN_Y, Coordinates.MAX_Y,
                            "У элемента некорректная координата, id: " + city.getId(), false)

                            || !checkGovernor(city.getGovernor(), city.getId()))

                        throw new InputFormatException();
                    if (!idList.add(city.getId()))
                        throw new SameIdException("В коллекции присутствуют элементы с одинаковыми id, будет загружен только один!!!");
                    this.collection.put(city.getId(), city);
                } catch (InputFormatException e) {
                    System.out.println("Ошибка во входном файле, элемент с некорректными полями не будет добавлен в коллекцию!!!");
                } catch (SameIdException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        setInitializationDate();
    }

    private boolean checkGovernor(Human h, Long cityId) {
        if (h != null) {
            return h.getName() != null && FieldsValidator.checkNumber((long) h.getName().length(), 1, Integer.MAX_VALUE,
                    "У элемента некорректное имя, id: " + cityId, false)
                    && FieldsValidator.checkNumber(h.getAge(), 1, 200,
                    "У элемента некорректный возраст, id: " + cityId, false)
                    && FieldsValidator.checkNumber(h.getHeight(), 1, 300,
                    "У элемента некорректный рост, id: " + cityId, false);
        }
        return true;
    }


    public void setCollection(ConcurrentHashMap<Long, City> collection) {
        this.collection = collection;
    }

    public void setInitializationDate() {
        initializationDate = LocalDateTime.now();
    }

    public ConcurrentHashMap<Long, City> getCollection() {
        return this.collection;
    }

    public HashSet<Long> getIdList() {
        return idList;
    }

    public void syncCollectionWithDb() {
        try {
            this.collection = databaseManager.getCollectionFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * используется для команды show
     */
    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }
}
