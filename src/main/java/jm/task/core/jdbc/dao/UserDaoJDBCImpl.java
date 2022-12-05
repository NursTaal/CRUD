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

    public void createUsersTable(){

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS usersTable " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(30), lastName VARCHAR(35), age INT);");

            System.out.println("Table create success");
            conn.commit();
        } catch (SQLException ignore) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Table create ERROR");
        }



    }

    public void dropUsersTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS usersTable;");
            conn.commit();
        } catch (SQLException ignored) {
            System.out.println("dropUserTable");
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("INSERT INTO usersTable (name, lastName, age) VALUES ('" + name + "', '" + lastName + "', " + age + ");");
            System.out.println("User с именем – " + name +" добавлен в базу данных" );
            conn.commit();
        } catch (SQLException e) {
            System.out.println(" Save user method ERROR");
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }
    }

    public void removeUserById(long id) {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM usersTable WHERE ID = " + id + ";");
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e.printStackTrace();
            }
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
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }
        return usersList;
    }

    public void cleanUsersTable() {
        try (Statement statement = conn.createStatement()){
            statement.executeUpdate("TRUNCATE TABLE usersTable;");
            conn.commit();
        } catch (SQLException ignored) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("cleanUpTable ERROR");
        }
    }
}
