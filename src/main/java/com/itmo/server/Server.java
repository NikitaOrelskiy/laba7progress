package com.itmo.server;

import ch.qos.logback.classic.Logger;
import com.itmo.app.Application;

import com.itmo.commands.Command;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.*;

import com.itmo.utils.SerializationManager;
import org.slf4j.LoggerFactory;

public class Server {
    private DatagramChannel channel;
    private SocketAddress address;
    private byte[] buffer;
    private SerializationManager<Command> commandSerializationManager = new SerializationManager<>();
    private SerializationManager<Response> responseSerializationManager = new SerializationManager<>();
    public static final Logger log =  (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Server.class);

    private static final int DEFAULT_BUFFER_SIZE = 65536;
    private static final int READ_POOL_SIZE = 2;

    public Server() {
        buffer = new byte[DEFAULT_BUFFER_SIZE];
    }

    //модуль приёма соединений
    public void connect(int port) throws IOException {
        address = new InetSocketAddress(port);
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.bind(address);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Сервер закончил работу...")));
        System.out.println("connect done");
    }

    //чтение полученных данных и отправка ответа
    public void run(Application application) {
        try {
            while(true) {
                ForkJoinPool commonPool = new ForkJoinPool(READ_POOL_SIZE);
                SocketAddress socketAddress = commonPool.invoke(new RecursiveTask<SocketAddress>() {
                    @Override
                    protected SocketAddress compute() {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
                        SocketAddress socketAddress = null;
                        do {
                            try {
                                socketAddress = channel.receive(byteBuffer);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } while (socketAddress == null);
                        return socketAddress;
                    }
                });
                byte[] copyData = new byte[buffer.length];
                System.arraycopy(buffer, 0, copyData, 0, buffer.length);
                new HandlerThread(application, copyData, socketAddress, channel).start();
            }
        } catch (ClassCastException e) {
            System.out.println("Сервер ожидал команду, а получил что-то не то...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
