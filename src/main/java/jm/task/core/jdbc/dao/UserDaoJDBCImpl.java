package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl extends Util implements UserDao {
    public UserDaoJDBCImpl() {

    }
    Connection conn = getConnection();

    public void createUsersTable() {

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS usersTable (id INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(30), lastName VARCHAR(35), age INT);");

            System.out.println("Table create success");
        } catch (SQLException ignore) {
            System.out.println("Table create ERROR");
        }


    }

    public void dropUsersTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS usersTable;");
        } catch (SQLException ignored) {
            System.out.println("dropUserTable");
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("INSERT INTO usersTable (name, lastName, age) VALUES ('" + name + "', '" + lastName + "', " + age + ");");
            System.out.println("User с именем – " + name +" добавлен в базу данных" );
        } catch (SQLException e) {
            System.out.println(" Save user method ERROR");
        }
    }

    public void removeUserById(long id) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM usersTable WHERE ID = " + id + ";");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        String sql = "SELECT name, lastName, age FROM usersTable;";

        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User user1 = new User();
                user1.setName(resultSet.getString("name"));
                user1.setLastName(resultSet.getString("lastName"));
                user1.setAge(resultSet.getByte("age"));

                usersList.add(user1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usersList;
    }

    public void cleanUsersTable() {
        try (Statement statement = conn.createStatement()){
            statement.executeUpdate("TRUNCATE TABLE usersTable;");
        } catch (SQLException ignored) {
            System.out.println("cleanUpTable ERROR");
        }
    }
}
