package com.itmo.commands;


import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.data.City;
import com.itmo.exceptions.InputFormatException;
import com.itmo.utils.FieldsValidator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InsertCommand extends Command implements CommandWithInit {

    protected City city;

    public InsertCommand() {
    }

    public void init(String argument, Scanner scanner) {
        city = executeInitialization(argument, scanner);
    }

    /**
     * исполнение
     *
     * @param application - текущее приложение
     * @param user пользователь
     */
    @Override
    public String execute(Application application, User user) {
        city.setOwner(user);
        if(!application.getDatabaseManager().addCity(city))
            return "Элемент не добавлен. Проверьте корректность данных";
        application.getIdList().add(city.getId());
        Map<Long, City> collection = application.getCollection();
        application.syncCollectionWithDb();
        application.setCollection(collection.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getValue().getName()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, ConcurrentHashMap::new)));

        return "Элемент с именем " + city.getName() + " добавлен в коллекцию";
    }

    public City executeInitialization(String argument, Scanner scanner) {
        City city = new City();
        if (scanner != null) city.setScanner(scanner);
        if (!FieldsValidator.checkNumber((long) argument.length(), 2, 19,
                "Некорректное имя элемента, оно должно быть из 2-19 знаков!!!", false))
            throw new InputFormatException();
        city.setName(argument);
        city.setFields();

        city.setScanner(null);
        return city;
    }


    @Override
    String getCommandInfo() {
        return "insert_null {element} : добавит новый элемент в коллекцию";
    }

    @Override
    public String toString() {
        return "insert_null";
    }

    @Override
    public boolean withArgument() {
        return true;
    }
}
