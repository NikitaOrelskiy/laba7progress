package com.itmo.commands;


import com.itmo.app.Application;
import com.itmo.client.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * абстрактный класс команды, описывает общее для всех команд поведение
 * также каждая новая команда должна переопределять toString - возвращать
 * представление команды без аргументов, нужно для истории и обработчика
 */
@Getter
@Setter
public abstract class Command implements Serializable {
    User user;

    /**
     * метод исполнения команды
     * @param application - текущее работающее приложение
     */
    public abstract String execute(Application application, User user);

    /**
     * @return информация о команде, которая потом выводится с командой help
     */
    abstract String getCommandInfo();

    /**
     * метод, нужный для того, чтобы из ссылки на абстрактную команды, мы знали требуется ли этой команде аргумент
     */
    public abstract boolean withArgument();
}
