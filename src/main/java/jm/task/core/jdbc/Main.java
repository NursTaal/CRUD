package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;

import static jm.task.core.jdbc.util.Util.getConnection;
import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class Main {


    public static void main(String[] args) throws SQLException {
        // реализуйте алгоритм здесь

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Name1", "LastName1", (byte) 20);
        userService.saveUser("Name2", "LastName2", (byte) 25);
        userService.saveUser("Name3", "LastName3", (byte) 31);
        userService.saveUser("Name4", "LastName4", (byte) 38);

        userService.removeUserById(1);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();

        getSessionFactory().close();
    }
}
/*
Требования к классам приложения:

 Классы dao/service должны реализовывать соответствующие интерфейсы
 Класс dao должен иметь конструктор пустой/по умолчанию
 Все поля должны быть private
 service переиспользует методы dao
 Обработка всех исключений, связанных с работой с базой данных должна находиться в dao
 Класс Util должен содержать логику настройки соединения с базой данных

Необходимые операции:

 Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует
 Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует
 Очистка содержания таблицы
 Добавление User в таблицу
 Удаление User из таблицы ( по id )
 Получение всех User(ов) из таблицы

Алгоритм работы приложения:

В методе main класса Main должны происходить следующие операции:
 Создание таблицы User(ов)
 Добавление 4 User(ов) в таблицу с данными на свой выбор. После каждого добавления должен быть вывод в консоль
 ( User с именем – name добавлен в базу данных )
 Получение всех User из базы и вывод в консоль ( должен быть переопределен toString в классе User)
 Очистка таблицы User(ов)
 Удаление таблицы

 */