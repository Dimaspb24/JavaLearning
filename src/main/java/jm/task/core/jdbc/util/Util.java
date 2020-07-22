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

    private static final SessionFactory sessionFactory = configureSessionFactory();

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


    /**
     * Получить фабрику сессий
     *
     * @return {@link SessionFactory}
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Создание фабрики
     *
     * @return {@link SessionFactory}
     * @throws HibernateException
     */
    private static SessionFactory configureSessionFactory() throws HibernateException {
        // Настройки hibernate
        Configuration configuration = new Configuration()
                // Database connection settings
                .setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver")
                .setProperty("hibernate.connection.url", MessageFormat.format(
                        "jdbc:mysql://{0}:{1}/{2}?useSSL={3}&allowPublicKeyRetrieval={4}&serverTimezone={5}",
                        HOST_NAME, PORT, TABLE_NAME, ACCESS_SSL, ACCESS_PUBLIC_KEY, SERVER_TIMEZONE))
                .setProperty("hibernate.connection.username", "root")
                .setProperty("hibernate.connection.password", "root!123")
                // JDBC connection pool (use the built-in)
                .setProperty("hibernate.connection.pool_size", "1")

                .setProperty("hibernate.connection.autocommit", "false")
                // Encoding
                .setProperty("hibernate.connection.characterEncoding", "utf8")
                .setProperty("hibernate.connection.CharSet", "utf8")
                .setProperty("hibernate.connection.useUnicode", "true")
                // Cache
                .setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider")
                .setProperty("hibernate.cache.use_second_level_cache", "false")
                .setProperty("hibernate.cache.use_query_cache", "false")
                // SQL dialect
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")

                // For debug
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "true")
                .setProperty("hibernate.use_sql_comments", "true")

                .setProperty("hibernate.current_session_context_class", "thread")
                .addAnnotatedClass(User.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
