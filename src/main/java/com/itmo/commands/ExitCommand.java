package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;


/**
 * команда закрытия приложения
 */
public class ExitCommand extends Command {
    private Application application;

    /**
     * установим флаг выхода, остальное условности
     */
    @Override
    public String execute(Application application, User user) {
        this.application = application;
        return "Выход из приложения...";
    }

    @Override
    String getCommandInfo() {
        return "exit : завершит работу клиентского приложения";
    }

    @Override
    public String toString() {
        return "exit";
    }

    @Override
    public boolean withArgument() {
        return false;
    }
}
