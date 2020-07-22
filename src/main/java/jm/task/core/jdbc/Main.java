package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Dima", "Bogdanov", (byte) 20);
        userService.saveUser("Ivan", "Wert", (byte) 21);
        userService.saveUser("Peter", "Dasw", (byte) 29);
        userService.saveUser("Karl", "Bram", (byte) 18);

        userService.removeUserById(2L);

        userService.getAllUsers().forEach(System.out::println);

//        userService.cleanUsersTable();
//        userService.dropUsersTable();
    }
}
