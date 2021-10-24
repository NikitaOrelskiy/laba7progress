package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.data.City;

import java.util.HashSet;
import java.util.Map;

/**
 * команда выводит все элементы коллекции на консоль
 */
public class ShowCommand extends Command {

    public ShowCommand() {
    }

    @Override
    public String execute(Application application, User user) {
        StringBuilder result = new StringBuilder();
        Map<Long, City> coll = application.getCollection();
        if (!coll.isEmpty()) {
            coll.forEach((id, city) ->
                    result.append(city.toString()).append("\n"));
            return result.deleteCharAt(result.length() - 1).toString();
        }
        return "Коллекция пуста!!!";
    }

    @Override
    String getCommandInfo() {
        return "show : выводит все элементы коллекции в строковом представлении";
    }

    @Override
    public String toString() {
        return "show";
    }

    @Override
    public boolean withArgument() {
        return false;
    }
}
