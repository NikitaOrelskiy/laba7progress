package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.Client;
import com.itmo.client.Main;
import com.itmo.client.User;
import com.itmo.data.City;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

/**
 * команда для выполнения скриптов
 */
public class ExecuteScriptCommand extends Command implements CommandWithInit {
    private String status;

    public ExecuteScriptCommand() {
    }

    public void init(String argument, Scanner scanner) {
        try {
            File file = new File(argument);
            Scanner fileScanner = new Scanner(file);
            Client client = Main.getActiveClient();
            client.incrementScriptCounter();
            client.getHandler().run(fileScanner);
            client.decrementScriptCounter();
            status = "Скрипт " + argument + " закончил исполнение";
            return;
        } catch (FileNotFoundException e) {
            System.out.println("Скрипт не найден...");
        } catch (NullPointerException e) {
            System.out.println("Не найден активный клиент...");
        }
        status="Такого скрипта не существует!!! Все скрипты должны лежать на одном уровне с jar или src.";
    }

    /**
     * во время исполнения обработчик приложения запускается со сканером файла, что позполяет читать данные из файла
     *
     * @param application - текущее приложения
     */
    @Override
    public String execute(Application application, User user) {
        return status;
    }

    @Override
    String getCommandInfo() {
        return "execute_script file_name : считает и исполнит скрипт из указанного файла";
    }

    @Override
    public String toString() {
        return "execute_script";
    }

    @Override
    public boolean withArgument() {
        return true;
    }
}
