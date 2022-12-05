package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

import java.util.ArrayList;
import java.util.List;
/*
 Требования к классам приложения:

1. UserHibernateDaoImpl должен реализовывать интерефейс UserDao
2. В класс Util должна быть добавлена конфигурация для Hibernate ( рядом с JDBC), без использования xml.
3. Service на этот раз использует реализацию dao через Hibernate
4. Методы создания и удаления таблицы пользователей в классе UserHibernateDaoImpl должны быть реализованы с помощью SQL.
 */
public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Session session = getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS usersTable " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT,name VARCHAR(30), lastName VARCHAR(35), age INT);")
                    .addEntity(User.class).executeUpdate();

            session.getTransaction().commit();
            System.out.println("UserTable create successful");
        } catch (HibernateException e) {
            System.out.println("Create UserTable ERROR");
            try {
                session.getTransaction().rollback();
            } catch (HibernateException e1) {
                e.printStackTrace();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS usersTable;")
                    .addEntity(User.class).executeUpdate();

            session.getTransaction().commit();
            System.out.println("Drop userTable successful");
        } catch (HibernateException e) {
            System.out.println("Drop UserTable ERROR");
            try {
                session.getTransaction().rollback();
            } catch (HibernateException e1) {
                e.printStackTrace();
            }
        } finally {
        session.close();
    }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = getSessionFactory().getCurrentSession();
        try {
            User user = new User(name, lastName, age);
            session.beginTransaction();
            session.save(user);

            session.getTransaction().commit();
            System.out.println("saveUser successful");
        } catch (HibernateException e) {
            System.out.println("saveUser ERROR");
            try {
                session.getTransaction().rollback();
            } catch (HibernateException e1) {
                e.printStackTrace();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.createQuery("delete User " +
                    "where id = " + id).executeUpdate();

            session.getTransaction().commit();
            System.out.println("removeUserById successful");
        } catch (HibernateException e) {
            System.out.println("removeUserById ERROR");
            try {
                session.getTransaction().rollback();
            } catch (HibernateException e1) {
                e.printStackTrace();
            }
        } finally {
            session.close();
        }

    }

    @Override
    public List<User> getAllUsers() {
        List userList = new ArrayList<>();
        Session session = getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            userList = session.createQuery("from User").getResultList();

            session.getTransaction().commit();
            System.out.println("getAllUsers successful");
        } catch (HibernateException e) {
            System.out.println("getAllUsers ERROR");
            try {
                session.getTransaction().rollback();
            } catch (HibernateException e1) {
                e.printStackTrace();
            }
        } finally {
            session.close();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Session session = getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM User ")
                    .executeUpdate();

            session.getTransaction().commit();
            System.out.println("CleanUsersTable successful");
        } catch (HibernateException e) {
            System.out.println("CleanUsersTable ERROR");
            try {
                session.getTransaction().rollback();
            } catch (HibernateException e1) {
                e.printStackTrace();
            }
        } finally {
            session.close();
        }
    }
}
/*
1) Подключение к базе данных берем отсюда (просто копируем и осмысливаем):
https://dzone.com/articles/hibernate-5-java-configuration-example
Меняем propertiesSetting.put(Environment.HBM2DDL_AUTO, "");
// теперь таблица не будет удаляться и создаваться автоматически
+ в утиле не забываем создать метод для закрытия фабрики сессий.
даже если в решении не используете xml файл удалите (если вдруг изучали его).

2)  В класс Юзер добавить аннотации:
@Entity
@Table(name = "User")
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id")

3) ЮзерСервис
UserDao userDaoHibernate = new UserDaoHibernateImpl();
//создаем объект класса UserDaoHibernateImpl для вызова методов UserDaoJDBCImpl
+ меняем в методах  userDao на UserDao userDaoHibernate = new UserDaoHibernateImpl();
//создаем объект класса UserDaoHibernateImpl для вызова методов UserDaoJDBCImpl

4) UserDaoHibernateImpl
Прайвет статик файнал поле - вызываем соединение с фабрикой сессий (очевидно, 1 раз)
Query query Query query = session.createSQLQuery для создание таблицы, удаления таблицы,
получения всех пользователей, очистки таблицы.
User user = для работы с пользователями (удаление и добавление пользователя)
Сессии создаем
try (Session session = Util.getSessionFactory().openSession()) {
    session.beginTransaction();
или
public void removeUserById(long id) {   //получаем id пользователя, которго надо удалить
    try (Session session = Util.getSessionFactory().openSession()) {    //пытаемся получить сессию, подключиться к базе
        transaction = session.beginTransaction();
и потом коммитим соответственно либо сессию, либо транзакцию
В эксепшине проверяем на null, если что-то есть, делаем rollback
Всем быстрого решения и понимания.
 */