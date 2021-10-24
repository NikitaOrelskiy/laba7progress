package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.data.City;
import com.itmo.exceptions.IdNotFoundException;
import com.itmo.exceptions.InputFormatException;
import com.itmo.utils.FieldsValidator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemoveGreaterKeyCommand extends Command implements CommandWithInit {
    private Map<Long, City> collection;
    private HashSet<Long> idList;
    private long k;

    public RemoveGreaterKeyCommand() {
    }


    public void init(String argument, Scanner scanner) {
        if (!FieldsValidator.checkStringParseToLong(argument, "ключ - это целое число!!!"))
            throw new InputFormatException();
        k = Long.parseLong(argument);
    }

    /**
     * удаление элемента
     */
    @Override
    public String execute(Application application, User user) {
        collection = application.getCollection();
        idList = application.getIdList();
        StringBuilder str = new StringBuilder();
        try {
            Set<Map.Entry<Long, City>> entries = collection.entrySet();
            HashMap<Long, City> shallowCopy = (HashMap<Long, City>) entries.stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            // получили копию collection, чтобы не удалять элементы из collection во время collection.stream
            Stream<Map.Entry<Long, City>> copy = shallowCopy.entrySet().stream()
                    .filter(e -> e.getValue().getId() > k && e.getValue().getOwner().getName().equals(user.getName()));

            copy.forEach(e -> {
                application.getDatabaseManager().remove(e.getKey());
                collection.remove(e.getKey());
                str.append("Элемент с ключем ").append(e.getKey()).append(" удалён из коллекции");
            });
            application.syncCollectionWithDb();
            if (str.toString().isEmpty()) {
                str.append("Нет городов, id которых больше ").append(k).append("\n");
            }
        } catch (IdNotFoundException e) {
            return e.getMessage();
        }
        return str.toString();
    }

    @Override
    String getCommandInfo() {
        return "remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный";
    }

    @Override
    public String toString() {
        return "remove_greater_key";
    }

    @Override
    public boolean withArgument() {
        return true;
    }
}
