package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {

            String sql =
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id       bigint auto_increment primary key," +
                            "name     varchar(50) not null check(name !='')," +
                            "lastName varchar(50) not null check(lastName !='')," +
                            "age      tinyint not null" +
                    ")";
            statement.execute(sql);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при создании таблицы в БД", e);
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {

            String sql = "DROP TABLE IF EXISTS users";
            statement.execute(sql);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при удалении таблицы из БД", e);
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT users (name, lastName, age) VALUES (?,?,?)";

        try (Connection connection = Util.getMySQLConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            int result = statement.executeUpdate();
            if (result != 1) {
                throw new SQLException("Ошибка в добавлении записи в таблицу");
            }

            System.out.printf("User с именем – %s добавлен в базу данных\n", name);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при сохранении пользователя в БД", e);
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {

            String sql = "DELETE FROM users WHERE id=" + id;
            int result = statement.executeUpdate(sql);

            System.out.printf("User with id = %d removed\n", id);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при удалении пользователя из БД", e);
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM users";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                users.add(new User(
                        rs.getString("name"),
                        rs.getString("lastName"),
                        (byte) rs.getInt("age")));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при получении пользователей из БД", e);
            e.printStackTrace();
        }

        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getMySQLConnection();
            Statement statement = connection.createStatement()) {
            String sql = "TRUNCATE TABLE users";
            statement.execute(sql);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при отчистке таблицы в БД", e);
            e.printStackTrace();
        }
    }
}
