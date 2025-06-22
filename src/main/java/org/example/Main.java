package org.example;

import org.model.User;
import org.repository.UserRepository;
import org.service.UserService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        Scanner con = new Scanner(System.in);

        while (true) {
            System.out.println("Управление пользователями");
            System.out.println("1 - Create");
            System.out.println("2 - Show All");
            System.out.println("3 - Show by id");
            System.out.println("4 - Update");
            System.out.println("5 - Delete");
            System.out.println("0 - Exit");

            int choice = con.nextInt();
            con.nextLine();

            switch (choice) {
                case 1:
                    createUsr(con, userService);
                    break;
            }
        }
    }

    private static void createUsr(Scanner con, UserService userService) {
        System.out.println("Name: ");
        String name = con.nextLine();

        System.out.println("email");
        String email = con.nextLine();

        System.out.println("Age: ");
        int age = con.nextInt();
        con.nextLine();

        User user = new User(null, age, name, email, null);
        User createdUser = userService.createUser(user);
        System.out.println(createdUser + " created");
    }

    private static void showAllUsr(UserService userService) {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users");
        } else {
            System.out.println("Users: ");
            users.forEach(System.out::println);
        }
    }

    private static void usrById(Scanner con, UserService userService) {
        System.out.println("Enter id: ");
        Long id = con.nextLong();
        con.nextLine();

        User user = userService.findUserById(id);
        if (user != null) {
            System.out.println("user - " + user);
        } else {
            System.out.println("user " + id + " not found");
        }
    }
    private static void updateUsr(Scanner con, UserService userService) {
        System.out.print("User Id to update: ");
        Long id = con.nextLong();
        con.nextLine();

        User existingUser = userService.findUserById(id);
        if (existingUser == null) {
            System.out.println("user " + id + " not found");
            return;
        }

        System.out.print("Enter new name (current: " + existingUser.getName() + "): ");
        String name = con.nextLine();

        System.out.print("Enter new email (current: " + existingUser.getEmail() + "): ");
        String email = con.nextLine();

        System.out.print("Enter new age (current: " + existingUser.getAge() + "): ");
        int age = con.nextInt();
        con.nextLine();

        User updatedUser = new User(id, age, name,email, existingUser.getCreated_at());
        userService.updateUser(id, updatedUser);
        System.out.println("User updated: " + updatedUser);
    }

    private static void deleteUsr(Scanner con, UserService userService) {
        System.out.print("User id to delete: ");
        Long id = con.nextLong();
        con.nextLine();
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            System.out.println("User with id " + id + " deleted");
        } else {
            System.out.println("User with id " + id + " not found");
        }
    }
}