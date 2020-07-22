package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    public static final String HOST_NAME = "localhost";
    public static final String TABLE_NAME = "base";
    public static final String USER_NAME = "root";
    public static final String USER_PASSWORD = "root!123";
    public static final String PORT = "3306";

    private static final String SERVER_TIMEZONE = "UTC";
    private static final String ACCESS_PUBLIC_KEY = "true";
    private static final String ACCESS_SSL = "false";
    private static final Logger logger = Logger.getLogger(Util.class.getName());

    public static Connection getMySQLConnection() throws SQLException {
        return getMySQLConnection(HOST_NAME, TABLE_NAME, USER_NAME, USER_PASSWORD);
    }

    public static Connection getMySQLConnection(String hostName, String dbName, String userName, String password)
            throws SQLException {
        try {
            String connectionURL = MessageFormat.format(
                    "jdbc:mysql://{0}:{1}/{2}?useSSL={3}&allowPublicKeyRetrieval={4}&serverTimezone={5}",
                    hostName, PORT, dbName, ACCESS_SSL, ACCESS_PUBLIC_KEY, SERVER_TIMEZONE);

            return DriverManager.getConnection(connectionURL, userName, password);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка в привязке или закрытии соединения с бд", e);
            throw new SQLException("Ошибка в подключении к базе данных");
        }
    }

}
