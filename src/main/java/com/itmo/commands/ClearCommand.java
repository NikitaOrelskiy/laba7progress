package com.itmo.commands;


import com.itmo.app.Application;
import com.itmo.client.User;


/**
 * команда очищает коллекцию
 */
public class ClearCommand extends Command {
    @Override
    public String execute(Application application, User user) {
        application.getDatabaseManager().removeAll(user.getName());
        application.getCollection().forEach((key, value) -> {
            if (value.getOwner().equals(user)) {
                application.getIdList().remove(key);
                application.getCollection().remove(key);
                application.getDatabaseManager().remove(key);
            }
        });
        application.syncCollectionWithDb();
        return "Все элементы принадлежашие пользователю " + user + " удалены.";
    }

    @Override
    String getCommandInfo() {
        return "clear : очистит коллекцию";
    }

    @Override
    public String toString() {
        return "clear";
    }

    @Override
    public boolean withArgument() {
        return false;
    }
}
