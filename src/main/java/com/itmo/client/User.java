package com.itmo.client;

import lombok.*;

import java.io.Serializable;

/**
 * класс пользователя
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {
    private String name;
    private String pass;


    @Override
    public String toString() {
        return name;
    }
}
