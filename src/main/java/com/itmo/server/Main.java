package com.itmo.server;

import ch.qos.logback.classic.Logger;
import com.itmo.app.Application;
import com.itmo.utils.FileManager;
import com.itmo.data.City;
import com.itmo.exceptions.WithoutArgumentException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Main {
    public static final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Main.class);

    public static String filename = "input.json";

    public static void main(String[] args) throws Exception {
        try {
            if (args.length == 0) throw new WithoutArgumentException();
            int port = Integer.parseInt(args[0]);
            if (args.length == 2) {
                filename = args[1];
            }
            Application application = new Application();
            System.out.println("Серверное приложение запущено...");
            Server server = new Server();
            server.connect(port);
            log.info("Connection is established, listen port: " + port);
            server.run(application);
        } catch (WithoutArgumentException e) {
            System.out.println("Для запуска введите порт в виде аргумента командной строки!!!");
        } catch (NumberFormatException e) {
            System.out.println("Порт - это целое число!!!");
        } catch (IOException e) {
            System.out.println("Проблемы с подключением...");
        }
    }
}
