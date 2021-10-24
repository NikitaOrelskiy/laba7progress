package com.itmo.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Coordinates implements Serializable {
    private int id;
    private Integer x; //Максимальное значение поля: 770, Поле не может быть null
    private double y; //Значение поля должно быть больше -590

    public static long MAX_X = 770;
    public static long MIN_X = Integer.MIN_VALUE;

    public static long MAX_Y = Integer.MAX_VALUE;
    public static long MIN_Y = -590;

    public Coordinates(Integer x, double y) {
        this.x = x;
        this.y = y;
    }
    public Coordinates(){}

    public Coordinates(int id, Integer x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
