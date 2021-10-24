package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.data.City;
import com.itmo.exceptions.IdNotFoundException;
import com.itmo.exceptions.InputFormatException;
import com.itmo.utils.FieldsValidator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemoveGreaterCommand extends Command implements CommandWithInit {

    private long val;

    public RemoveGreaterCommand() {
    }


    public void init(String argument, Scanner scanner) {
        if (!FieldsValidator.checkStringParseToLong(argument, "значение элемента - это целое число!!!"))
            throw new InputFormatException();
        val = Long.parseLong(argument);
    }

    /**
     * удаление элемента
     */
    @Override
    public String execute(Application application, User user) {
        ConcurrentHashMap<Long, City> collection = application.getCollection();
        StringBuilder str = new StringBuilder();
        try {
            Set<ConcurrentHashMap.Entry<Long, City>> entries = collection.entrySet();
            HashMap<Long, City> shallowCopy = (HashMap<Long, City>) entries.stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            // получили копию collection, чтобы не удалять элементы из collection во время collection.stream
            Stream<Map.Entry<Long, City>> copy = shallowCopy.entrySet().stream()
                    .filter(e -> e.getValue().getArea() > val && e.getValue().getOwner().getName()
                            .equals(user.getName()));

            copy.forEach(e -> {
                collection.remove(e.getKey());
                application.getDatabaseManager().remove(e.getKey());
                str.append("Элемент с ключем ").append(e.getKey()).append(" удалён из коллекции\n");
            });
            application.syncCollectionWithDb();
            if (str.toString().isEmpty()) {
                str.append("Нет городов, area которых больше ").append(val).append("\n");
            }
        } catch (IdNotFoundException e) {
            return e.getMessage();
        }
        return str.toString();
    }


    @Override
    String getCommandInfo() {
        return "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public String toString() {
        return "remove_greater";
    }

    @Override
    public boolean withArgument() {
        return true;
    }
}

