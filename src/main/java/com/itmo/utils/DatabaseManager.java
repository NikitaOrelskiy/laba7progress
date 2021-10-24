package com.itmo.utils;

import com.itmo.client.User;
import com.itmo.data.City;
import com.itmo.data.Climate;
import com.itmo.data.Coordinates;
import com.itmo.data.Human;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Синглтон для работы с базой данных
 * Исполнение запросов и т.п.
 */
public class DatabaseManager {
    //For Database
    private static final String DB_URL = "jdbc:postgresql://pg:5432/studs";
    //private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static String USER;
    private static String PASS;
    private static final String FILE_WITH_ACCOUNT = "account.txt";
    private static final String CITY_TABLE = "city";
    private static final String USERS_TABLE = "users";
    private static final String COORDINATES_TABLE = "coordinates";
    private static final String HUMAN_TABLE = "human";
    private static final String pepper = "1@#$&^%$)3";
    private Connection connection;
    private PassEncoder passEncoder;

    //читаем данные аккаунта для входа подключения к бд, ищем драйвер
    static {
        try (FileReader fileReader = new FileReader(FILE_WITH_ACCOUNT);
             BufferedReader reader = new BufferedReader(fileReader)) {
            USER = reader.readLine();
            PASS = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Connection to PostgreSQL JDBC");
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL JDBC Driver successfully connected");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path");
            e.printStackTrace();
        }
    }


    public DatabaseManager(String dbUrl, String user, String pass) {
        try {
            connection = DriverManager.getConnection(dbUrl, user, pass);
            passEncoder = new PassEncoder(pepper);
        } catch (SQLException e) {
            System.out.println("Connection to database failed");
            e.printStackTrace();
        }
    }

    public DatabaseManager(String address, int port, String dbName, String user, String pass) {
        this("jdbc:postgresql://" + address + ":" + port + "/" + dbName, user, pass);
    }

    public DatabaseManager() {
        this(DB_URL, USER, PASS);
    }

    //загрузка коллекции в память
    public ConcurrentHashMap<Long, City> getCollectionFromDatabase() throws SQLException {
        ConcurrentHashMap<Long, City> collection = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, Human> humans = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, Coordinates> coordinates = new ConcurrentHashMap<>();

        PreparedStatement cityStatement = connection.prepareStatement("select * from " + CITY_TABLE);
        ResultSet cityResult = cityStatement.executeQuery();

        PreparedStatement humanStatement = connection.prepareStatement("select * from " + HUMAN_TABLE);
        ResultSet hr = humanStatement.executeQuery();

        PreparedStatement coordinatesStatement = connection.prepareStatement("select * from " + COORDINATES_TABLE);
        ResultSet cr = coordinatesStatement.executeQuery();


        while (cr.next()) {
            int id = cr.getInt("id");
            Coordinates curr = new Coordinates(id, cr.getInt("x"), cr.getDouble("y"));
            coordinates.put(id, curr);
        }

        while (hr.next()) {
            int id = hr.getInt("id");
            Human curr = new Human(id, hr.getString("name"), hr.getLong("age"),
                    hr.getLong("height"), hr.getDate("birthday").toLocalDate());
            humans.put(id, curr);
        }

        while (cityResult.next()) {
            Long cityId = cityResult.getLong("id");
            String name = cityResult.getString("name");
            LocalDate creationDate = cityResult.getDate("creation_date").toLocalDate();
            long area = cityResult.getLong("area");
            Integer population = cityResult.getInt("population");
            long metersAboveSea = cityResult.getLong("meters_above_sea");
            LocalDate establishmentDate = cityResult.getDate("establishment_date").toLocalDate();
            float agglomeration = cityResult.getFloat("agglomeration");
            String climateStr = cityResult.getString("climate");
            Climate climate = null;
            if (climateStr != null) climate = Climate.valueOf(climateStr);

            int coordsId = cityResult.getInt("coordinates_id");
            int governorId = cityResult.getInt("governor_id");

            City city = new City(cityId, name, coordinates.get(coordsId), creationDate, area, population, metersAboveSea,
                    establishmentDate, agglomeration, climate, humans.get(governorId));

            collection.put(cityId, city);
        }
        return collection;
    }

    //добаление нового элемента
    public boolean addCity(City city) {
        try {

            PreparedStatement coordStatement = connection.prepareStatement("insert into " + COORDINATES_TABLE +
                    "(x, y) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            coordStatement.setInt(1, city.getCoordinates().getX());
            coordStatement.setDouble(2, city.getCoordinates().getY());
            coordStatement.executeUpdate();
            ResultSet coordRs = coordStatement.getGeneratedKeys();
            coordRs.next();
            int coordId = coordRs.getInt(1);
            System.out.println("coordsid" + coordId);

            Human gov = city.getGovernor();
            int govId = -1;
            if (gov != null) {
                PreparedStatement humanStatement = connection.prepareStatement("insert into " + HUMAN_TABLE +
                        "(name, age, height, birthday) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                humanStatement.setString(1, gov.getName());
                humanStatement.setLong(2, gov.getAge());
                humanStatement.setLong(3, gov.getHeight());
                humanStatement.setDate(4, Date.valueOf(gov.getBirthday()));
                humanStatement.executeUpdate();
                ResultSet humanRs = humanStatement.getGeneratedKeys();
                if (humanRs.next()) {
                    govId = humanRs.getInt(1);
                } else {
                    System.out.println("humanRs empty");
                }
            }


            PreparedStatement cityStatement = connection.prepareStatement("insert into " + CITY_TABLE +
                    "(name, coordinates_id, creation_date, area, population, meters_above_sea, establishment_date, " +
                    "agglomeration, climate, governor_id, owner) " +
                    "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            cityStatement.setString(1, city.getName());
            cityStatement.setInt(2, coordId);
            cityStatement.setDate(3, Date.valueOf(city.getCreationDate()));
            cityStatement.setLong(4, city.getArea());
            cityStatement.setInt(5, city.getPopulation());
            cityStatement.setLong(6, city.getMetersAboveSeaLevel());
            cityStatement.setDate(7, Date.valueOf(city.getEstablishmentDate()));
            cityStatement.setFloat(8, city.getAgglomeration());
            if(city.getClimate()!= null) cityStatement.setString(9, city.getClimate().toString());
            else cityStatement.setNull(9, Types.VARCHAR);
            if (govId != -1) {
                cityStatement.setInt(10, govId);
            } else {
                cityStatement.setNull(10, Types.INTEGER);
            }
            cityStatement.setString(11, city.getOwner().getName());
            cityStatement.executeUpdate();
            ResultSet cityRs = cityStatement.getGeneratedKeys();
            cityRs.next();
            city.setId(cityRs.getLong(1));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // todo
    //удаление элемента по id
    public int remove(long id) {
        try {
            PreparedStatement get = connection.prepareStatement("select * from " + CITY_TABLE + " where id = ?");
            get.setLong(1, id);
            ResultSet hr = get.executeQuery();
            if(hr.next()){
                int guvId = hr.getInt("governor_id");
                int corrId = hr.getInt("coordinates_id");
                PreparedStatement statement = connection.prepareStatement("delete from " + CITY_TABLE + " where id = ?");
                statement.setLong(1, id);
                statement.executeUpdate();

                if (guvId != 0 ){
                    PreparedStatement guv = connection.prepareStatement("delete from " + HUMAN_TABLE + " where id = ?");
                    guv.setLong(1, guvId);
                    guv.executeUpdate();
                }
                if (corrId != 0){
                    PreparedStatement corr = connection.prepareStatement("delete from " + COORDINATES_TABLE + " where id=?");
                    corr.setLong(1, corrId);
                    corr.executeUpdate();
                }
                return 1;
            }
            else return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // todo
    //удаляем все элементы, принадлежащие пользователю
    public boolean removeAll(String userName) {
        try {
            String sql = "DELETE FROM coordinates cd " +
                    "USING  city c " +
                    "WHERE  cd.id = c.coordinates_id" +
                    "AND    c.owner_name = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //обновляем поля элемента
    public int update(long id, City city) {
        try {

            PreparedStatement coordStatement = connection.prepareStatement("update " + COORDINATES_TABLE +
                    " set x = ?, y =  ?");
            coordStatement.setInt(1, city.getCoordinates().getX());
            coordStatement.setDouble(2, city.getCoordinates().getY());
            coordStatement.executeUpdate();

            Human gov = city.getGovernor();

            if (gov != null) {
                PreparedStatement humanStatement = connection.prepareStatement("update " + HUMAN_TABLE +
                        " set name = ?, age = ?, height = ?, birthday = ?)");
                humanStatement.setString(1, gov.getName());
                humanStatement.setLong(2, gov.getAge());
                humanStatement.setLong(3, gov.getHeight());
                humanStatement.setDate(4, Date.valueOf(gov.getBirthday()));
                humanStatement.executeUpdate();
            }

            PreparedStatement cityStatement = connection.prepareStatement("update " + CITY_TABLE +
                    "set name = ?, coordinates_id = ?, creation_date = ?, area = ?, population = ?," +
                    " meters_above_sea = ?, establishment_date = ?, " +
                    "agglomeration = ?, climate = ?, governor_id = ?");
            cityStatement.setString(1, city.getName());
            cityStatement.setInt(2, city.getCoordinates().getId());
            cityStatement.setDate(3, Date.valueOf(city.getCreationDate()));
            cityStatement.setLong(4, city.getArea());
            cityStatement.setInt(5, city.getPopulation());
            cityStatement.setLong(6, city.getMetersAboveSeaLevel());
            cityStatement.setDate(7, Date.valueOf(city.getEstablishmentDate()));
            cityStatement.setFloat(8, city.getAgglomeration());
            cityStatement.setString(9, city.getClimate().toString());
            if (gov != null) {
                cityStatement.setInt(10, gov.getId());
            } else {
                cityStatement.setNull(10, Types.INTEGER);
            }
            return cityStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //добавление нового пользователя
    public void addUser(User user) {
        String salt = new SimplePasswordGenerator(true, true, true, true).generate(10, 10);
        String hash = passEncoder.getHash(user.getPass() + salt);
        try {
            PreparedStatement statement = connection.prepareStatement("insert into " + USERS_TABLE +
                    "(name, hash, salt) values (?, ?, ?)");
            statement.setString(1, user.getName());
            statement.setString(2, hash);
            statement.setString(3, salt);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ищем пользователя
    public boolean containsUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from " + USERS_TABLE + " where name = ?");
            statement.setString(1, user.getName());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return false;
            String salt = resultSet.getString("salt");
            String hash = passEncoder.getHash(user.getPass() + salt);
            statement = connection.prepareStatement("select * from " + USERS_TABLE + " where name = ? and hash = ? and salt=?");
            statement.setString(1, user.getName());
            statement.setString(2, hash);
            statement.setString(3, salt);
            return statement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //ищем пользователя только по имени
    public boolean containsUserName(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from " + USERS_TABLE + " where name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

