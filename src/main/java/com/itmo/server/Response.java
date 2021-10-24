package com.itmo.server;

import com.itmo.client.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class Response implements Serializable {
    private String answer;
    private User user;

    public Response(String answer) {
        this.answer = answer;
    }

    public Response(String answer, User user) {
        this.answer = answer;
        this.user = user;
    }
}
