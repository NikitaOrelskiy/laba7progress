package com.itmo.commands;

import com.itmo.app.Application;
import com.itmo.client.User;

public class WhoAmICommand extends Command{
    @Override
    public String execute(Application application, User user) {
        return user.getName();
    }

    @Override
    public String toString() {
        return "whoami";
    }

    @Override
    String getCommandInfo() {
        return "узнать имя пользователя";
    }

    @Override
    public boolean withArgument() {
        return false;
    }
}
