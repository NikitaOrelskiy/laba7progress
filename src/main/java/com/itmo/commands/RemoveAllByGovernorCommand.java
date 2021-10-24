package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.data.City;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemoveAllByGovernorCommand extends Command implements CommandWithInit {
    private Map<Long, City> collection;
    private HashSet<Long> idList;
    private String govName;

    public RemoveAllByGovernorCommand() {
    }

    public void init(String argument, Scanner scanner) {
        govName = argument;
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
            application.syncCollectionWithDb();
            Set<Map.Entry<Long, City>> entries = collection.entrySet();
            HashMap<Long, City> shallowCopy = (HashMap<Long, City>) entries.stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            // получили копию collection, чтобы не удалять элементы из collection во время collection.stream
            Stream<Map.Entry<Long, City>> copy = shallowCopy.entrySet().stream()
                    .filter(el -> govName.equals(el.getValue().getGovernor().getName())
                    && el.getValue().getOwner().getName().equals(user.getName()));

            copy.forEach(e -> {
                collection.remove(e.getKey());
                application.getDatabaseManager().remove(e.getKey());
                str.append("Элемент с ключем ").append(e.getKey()).append(" удалён из коллекции");
            });

            if (str.toString().isEmpty()) {
                str.append("Нет таких городов, у которых имя governor ").append(govName).append("\n");
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return str.toString();
    }


    @Override
    String getCommandInfo() {
        return "remove_all_by_governor governor_name : удалить из коллекции все элементы, значение поля governor которого эквивалентно заданному";
    }

    @Override
    public String toString() {
        return "remove_all_by_governor";
    }

    @Override
    public boolean withArgument() {
        return true;
    }

}
