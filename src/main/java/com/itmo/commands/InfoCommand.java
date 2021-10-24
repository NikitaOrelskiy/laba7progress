package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.data.City;

import java.util.HashSet;
import java.util.Map;

/**
 * команда выводит информацию о коллекции
 */
public class InfoCommand extends Command {

    @Override
    public String execute(Application application, User user) {
        StringBuilder result = new StringBuilder();
        result.append("Информация о коллекции:").append("\n");
        result.append("Дата создания: ").append(application.getInitializationDate()).append("\n");
        result.append("Кол-во элементов: ").append(application.getCollection().size()).append("\n");
        if (!application.getCollection().isEmpty()) {
            result.append("Тип данных, хранимых в коллекции: ")
                    .append(application.getCollection().getClass()).append("\n");
        }
        return result.deleteCharAt(result.length()-1).toString();
    }


    @Override
    String getCommandInfo() {
        return "info : выводит информацию о коллекции";
    }

    @Override
    public String toString() {
        return "info";
    }

    @Override
    public boolean withArgument() {
        return false;
    }
}
