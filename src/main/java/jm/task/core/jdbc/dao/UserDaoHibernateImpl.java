package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private final Logger logger = Logger.getLogger(UserDaoHibernateImpl.class.getName());

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql =
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id       bigint auto_increment primary key," +
                            "name     varchar(50) not null check(name !='')," +
                            "lastName varchar(50) not null check(lastName !='')," +
                            "age      tinyint not null" +
                    ")";
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "Ошибка при создании таблицы в БД", e);
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS users";
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "Ошибка при удалении таблицы из БД", e);
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "Ошибка при сохранении пользователя в БД", e);
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE User WHERE id = " + id).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "Ошибка при удалении пользователя из БД", e);
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            return (List<User>) session.createQuery("FROM User").list();
        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "Ошибка при получении пользователей из БД", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "Ошибка при отчистке таблицы в БД", e);
            e.printStackTrace();
        }
    }
}
