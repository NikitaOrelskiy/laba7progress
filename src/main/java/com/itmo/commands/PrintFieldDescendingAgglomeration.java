package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.data.City;
import com.itmo.data.Human;

import java.util.*;

public class PrintFieldDescendingAgglomeration extends Command {
    private Map<Long, City> collection;
    private HashSet<Long> idList;


    public PrintFieldDescendingAgglomeration() {
    }

    @Override
    public String execute(Application application, User user) {
        collection = application.getCollection();
        idList = application.getIdList();
        StringBuilder str = new StringBuilder();
        collection.values().stream()
                .sorted(Comparator.comparingLong(c -> {
                    Human g = c.getGovernor();
                    if(g == null) return 0;
                    return g.getHeight();
                }))
                .forEach(c -> str.append(c).append("\n"));
        return str.toString();
    }


    @Override
    String getCommandInfo() {
        return "print_field_descending_agglomeration : вывести " +
                "значения поля agglomeration всех элементов в порядке убывания";
    }

    @Override
    public String toString() {
        return "print_field_descending_agglomeration";
    }

    @Override
    public boolean withArgument() {
        return false;
    }
}
