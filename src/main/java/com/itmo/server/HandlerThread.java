package com.itmo.server;

import ch.qos.logback.classic.Logger;
import com.itmo.app.Application;
import com.itmo.commands.Command;
import com.itmo.utils.SerializationManager;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

/**
 * поток для обработки запроса от клиента
 */
@AllArgsConstructor
public class HandlerThread extends Thread {
    private Application application;
    private byte[] data;
    private SocketAddress socketAddress;
    private DatagramChannel datagramChannel;
    public static final Logger log =  (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(HandlerThread.class);

    @Override
    public void run() {
        try {
            Command command = new SerializationManager<Command>().readObject(data);
            log.info("Server receive command " + command.toString());
            String result;
            result = command.execute(application, command.getUser());
            Response response = new Response(result, command.getUser());
            log.info("Command " + command + " is completed, send an answer to the client");
            new SenderThread(datagramChannel, socketAddress, response).start();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
