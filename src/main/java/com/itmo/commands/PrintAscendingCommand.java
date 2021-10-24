package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.data.City;

import java.util.*;

public class PrintAscendingCommand extends Command {
    private Map<Long, City> collection;
    private HashSet<Long> idList;


    public PrintAscendingCommand() {
    }

    @Override
    public String execute(Application application, User user) {
        collection = application.getCollection();
        idList = application.getIdList();
        StringBuilder str = new StringBuilder();
        collection.values().stream().sorted(Comparator.comparingLong(City::getArea))
                .forEach(c -> str.append(c).append("\n"));
        if(str.toString().isEmpty()) str.append("Коллекция пуста!!!");
        return str.toString();
    }



    @Override
    String getCommandInfo() {
        return "print_ascending : вывести элементы коллекции в порядке возрастания";
    }

    @Override
    public String toString() {
        return "print_ascending";
    }

    @Override
    public boolean withArgument() {
        return false;
    }
}
