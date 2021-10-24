package com.itmo.data;


import com.itmo.client.User;
import com.itmo.utils.FieldsValidator;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

@Getter
public class City implements Comparable<City>, Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long area; //Значение поля должно быть больше 0
    private Integer population; //Значение поля должно быть больше 0, Поле не может быть null
    private long metersAboveSeaLevel;
    private java.time.LocalDate establishmentDate;
    private float agglomeration;
    private Climate climate; //Поле может быть null
    private Human governor; //Поле может быть null
    private Scanner in = new Scanner(System.in);
    private User owner;


    public City(Long id, String name, Coordinates coordinates,
                LocalDate creationDate, long area, Integer population,
                long metersAboveSeaLevel, LocalDate establishmentDate,
                float agglomeration, Climate climate, Human governor) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.establishmentDate = establishmentDate;
        this.agglomeration = agglomeration;
        this.climate = climate;
        this.governor = governor;
    }

    public City() {
    }

    @Override
    public String toString() {
        return "(" + owner + ") City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates.toString() +
                ", creationDate=" + creationDate +
                ", area=" + area +
                ", population=" + population +
                ", metersAboveSeaLevel=" + metersAboveSeaLevel +
                (establishmentDate != null ? (", establishmentDate=" + establishmentDate)
                        : "") +
                ", agglomeration=" + agglomeration +
                (climate != null ? (", climate=" + climate) : "") +
                (governor != null ? (", governor=" + governor.toString()) : "") +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(City o) {
        if (this.name.compareTo(o.name) != 0) {
            return this.name.compareTo(o.name);
        }
        return this.population.compareTo(o.population);
    }

    public Long getId() {
        return id;
    }

    public float getAgglomeration() {
        return agglomeration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public long getArea() {
        return area;
    }

    public void setArea(long area) {
        this.area = area;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public long getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    public void setMetersAboveSeaLevel(long metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
    }

    public Climate getClimate() {
        return climate;
    }

    public void setClimate(Climate climate) {
        this.climate = climate;
    }

    public Human getGovernor() {
        return governor;
    }

    public void setGovernor(Human governor) {
        this.governor = governor;
    }

    public void setScanner(Scanner scanner) {
        in = scanner;
    }

    public void setOwner(User user){
        this.owner = user;
    }

    /**
     * генерация случайного и уникального идентификатора города
     *
     * @param idList - лист идентификаторов, относительно которого id должен быть уникален
     */
    public static long generateId(HashSet<Long> idList) {
        Random random = new Random();
        boolean goodId = false;
        long id = Long.MAX_VALUE;
        while (!goodId) {
            id = random.nextLong();
            if (id <= 0) continue;
            goodId = idList.add(id);
        }
        return id;
    }

    public City(String name, Coordinates coordinates, LocalDate creationDate, long area, Integer population,
                long metersAboveSeaLevel, LocalDate establishmentDate, float agglomeration,
                Climate climate, Human governor) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.establishmentDate = establishmentDate;
        this.agglomeration = agglomeration;
        this.climate = climate;
        this.governor = governor;
    }

    public void setFields() {
        checkCoordinates();
        generateCreationDate();
        checkArea();
        checkPopulation();
        checkMetersAboveSeaLevel();
        checkEstablishmentDate();
        checkAgglomeration();
        checkClimate();
        checkGovernor();
    }

    private void checkGovernor() {
        String nextLine;
        do {
            System.out.println("Введите имя Губернатора: ");
            nextLine = in.nextLine();
        } while (!nextLine.equals("") && !FieldsValidator.checkNumber((long) nextLine.length(), 2, 19,
                "Неккоректное имя!!! Попробуйте снова. (оно должно состоять из 2-19 знаков)", false)
        );
        if (nextLine.equals("")) {
            System.out.println("поле воспринято как null");
            this.governor = null;
        } else {
            Human gov = new Human();
            gov.setName(nextLine);
            do {
                System.out.println("Введите возраст губернатора: ");
                nextLine = in.nextLine();
            } while ((!FieldsValidator.checkStringParseToInt(nextLine, "Возраст должен быть числом")
                    || !FieldsValidator.checkNumber(Long.parseLong(nextLine), 1, Integer.MAX_VALUE,
                    "возраст губернатора должен быть больше 0", false)));
            gov.setAge(Integer.parseInt(nextLine));
            do {
                System.out.println("Введите рост губернатора: ");
                nextLine = in.nextLine();
            } while ((!FieldsValidator.checkStringParseToInt(nextLine, "Рост должен быть числом")
                    || !FieldsValidator.checkNumber(Long.parseLong(nextLine), 1, Integer.MAX_VALUE,
                    "рост губернатора должен быть больше 0", false)));
            gov.setHeight(Integer.parseInt(nextLine));

            do {
                System.out.println("Введите день рождения губернатора dd-MM-YYYY: ");
                nextLine = in.nextLine();
            } while (!nextLine.equals("") && !FieldsValidator.checkDate(nextLine));
            gov.setBirth(nextLine);
            this.governor = gov;
        }
    }

    private void checkClimate() {
        String nextLine;
        do {
            System.out.println("Введите климат: TROPICAL_SAVANNA, OCEANIC, SUBARCTIC");
            nextLine = in.nextLine();
        } while (Climate.getValue(nextLine,
                "Вводите корректный климат из предложенных вариантов!!!") == null && !nextLine.equals(""));
        if (nextLine.equals("")) {
            this.climate = null;
            System.out.println("Значение поля воспринято как null");
        } else this.climate = Climate.getValue(nextLine, "");
    }

    private void checkAgglomeration() {
        String nextLine;
        do {
            System.out.println("Введите аггломерацию (float): ");
            nextLine = in.nextLine();
        } while (!FieldsValidator.checkStringParseToDouble(nextLine, "Ошибка ввода, аггломерация - это число!!! "));
        this.agglomeration = Float.parseFloat(nextLine);
    }

    private void checkEstablishmentDate() {
        String nextLine;
        do {
            System.out.println("Введите дату основания dd-MM-YYYY: ");
            nextLine = in.nextLine();
        } while (!FieldsValidator.checkDate(nextLine));
        this.establishmentDate = FieldsValidator.getDate(nextLine);
    }

    private void checkMetersAboveSeaLevel() {
        String nextLine;
        do {
            System.out.println("Введите метров над уровнем моря: ");
            nextLine = in.nextLine();
        } while (!FieldsValidator.checkStringParseToInt(nextLine, "Ошибка ввода, население - это число!!! "));
        this.metersAboveSeaLevel = Integer.parseInt(nextLine);
    }

    private void checkPopulation() {
        String nextLine;
        do {
            System.out.println("Введите количество населения: ");
            nextLine = in.nextLine();
        } while ((!FieldsValidator.checkStringParseToLong(nextLine, "Ошибка ввода, население - это число!!! ") ||
                !FieldsValidator.checkNumber(Long.parseLong(nextLine), 1, Long.MAX_VALUE,
                        "Население должна быть больше 0", false)));
        this.population = Integer.parseInt(nextLine);
    }

    private void checkArea() {
        String nextLine;
        do {
            System.out.println("Введите площадь: ");
            nextLine = in.nextLine();
        } while ((!FieldsValidator.checkStringParseToInt(nextLine, "Ошибка ввода, площадь - это число!!! ") ||
                !FieldsValidator.checkNumber((long) Integer.parseInt(nextLine), 1, Integer.MAX_VALUE,
                        "Площадь должна быть больше 0", false)));
        this.area = Integer.parseInt(nextLine);
    }

    private void generateCreationDate() {
        this.creationDate = LocalDate.now();
    }

    private void checkCoordinates() {
        String nextLine;
        do {
            System.out.println("Введите координату Х: ");
            nextLine = in.nextLine();
        } while (!FieldsValidator.checkStringParseToInt(nextLine,
                "Ошибка ввода, координата - это целое число!!! Попробуйте снова."));
        int x = Integer.parseInt(nextLine);
        do {
            System.out.println("Введите координату Y: ");
            nextLine = in.nextLine();
        } while (!FieldsValidator.checkStringParseToDouble(nextLine, "Ошибка ввода, координата - это число!!! ") ||
                !FieldsValidator.checkNumber((long) Double.parseDouble(nextLine), Coordinates.MIN_Y, Coordinates.MAX_Y,
                        "Координата слишком мала", false));
        double y = Double.parseDouble(nextLine);
        this.coordinates = new Coordinates(x, y);
    }

    public void checkName() {
        String nextLine;
        do {
            System.out.println("Введите имя города: ");
            nextLine = in.nextLine();
        } while (!FieldsValidator.checkNumber((long) nextLine.length(), 2, 19,
                "Неккоректное имя!!! Попробуйте снова. (оно должно состоять из 2-19 знаков)", false));
    }
}
