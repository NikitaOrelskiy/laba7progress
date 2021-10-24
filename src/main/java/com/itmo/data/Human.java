package com.itmo.data;

import com.itmo.utils.FieldsValidator;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

import java.util.Objects;

@Getter
@Setter
public class Human implements Serializable {
    private int id;

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public void setBirth(String birthday) {
        this.birthday = FieldsValidator.getDate(birthday);
    }

    private String name; //Поле не может быть null, Строка не может быть пустой
    private long age; //Значение поля должно быть больше 0
    private long height; //Значение поля должно быть больше 0
    private java.time.LocalDate birthday;

    public Human(int id, String name, long age, long height, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.birthday = birthday;
    }

    public Human(String name, long height, String birthday) {
        this.name = name;
        this.height = height;
        setBirth(birthday);
    }

    public Human(String name, long age, long height, LocalDate birthday) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.birthday = birthday;
    }
    public Human(String name, long age, long height, String birthday) {
        this.name = name;
        this.age = age;
        this.height = height;
        setBirth(birthday);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return height == human.height && name.equals(human.name) && birthday.equals(human.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, height, birthday);
    }

    public Human(){}

    public String getName() {
        return name;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public long getHeight() {
        return height;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return "Human{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", birthday=" + birthday +
                '}';
    }
}

