package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;
import com.itmo.utils.FieldsValidator;

import java.io.Console;
import java.util.Scanner;

/**
 * команда для авторизации пользователя
 */
public class LoginCommand extends Command implements CommandWithInit {
    private User userForLogin;

    //авторизация, если такой пользователь зарегистрирован
    @Override
    public String execute(Application application, User user) {
        if (application.getDatabaseManager().containsUser(userForLogin)) {
            this.user = userForLogin;
            return "Пользователь с ником " + userForLogin.getName() + " успешно авторизован\n" +
                    "Теперь вам доступны все команды, для их просмотра введите help";
        }
        this.user = null;
        return "Пароль или логин некорректен, авторизация отменена";
    }

    @Override
    public String getCommandInfo() {
        return "login name : авторизоваться по имени name";
    }

    @Override
    public String toString() {
        return "login";
    }

    @Override
    public boolean withArgument() {
        return true;
    }

    //просим пароль
    @Override
    public void init(String argument, Scanner scanner) {
        String pass;
        Console console = System.console();
        do {
            System.out.println("Введите пароль для авторизации(от 6 до 20 символов):");
            pass = new String(console.readPassword());
        } while (!FieldsValidator.checkNumber((long) pass.length(), 6, 19,
                "Пароль не удовлетворяет условиям. Попробуйте снова", false));
        userForLogin = new User(argument, pass);
    }
}
