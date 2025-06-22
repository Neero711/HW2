package org.example;

import org.model.User;
import org.repository.UserRepository;
import org.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting User CRUD application");
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

            try {
                int choice = con.nextInt();

                con.nextLine();

                switch (choice) {
                    case 1:
                        createUsr(con, userService);
                        break;
                    case 2:
                        showAllUsr(userService);
                        break;
                    case 3:
                        usrById(con, userService);
                        break;
                    case 4:
                        updateUsr(con, userService);
                        break;
                    case 5:
                        deleteUsr(con, userService);
                        break;
                    case 0:
                        System.out.println("Exit");
                        logger.info("Application shutdown");
                        return;
                    default:
                        System.out.println("Error");
                        logger.warn("Invalid user choice: {}", choice);
                }
            } catch (Exception e) {
                logger.error("Error in main loop: ", e);
                System.out.println("error: " + e.getMessage());
                con.nextLine();
            }
        }
    }

    private static void createUsr(Scanner con, UserService userService) {
        try {
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
        } catch (Exception e) {
            logger.error("Error creating user: ", e);
            System.out.println("User creation error: " + e.getMessage());
        }
    }

    private static void showAllUsr(UserService userService) {
        try {
            List<User> users = userService.findAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users");
                logger.info("No users found");
            } else {
                System.out.println("Users: ");
                users.forEach(System.out::println);
                logger.debug("Displayed {} users", users.size());
            }
        } catch (Exception e) {
            logger.error("Error retrieving users: ", e);
            System.out.println("User list error: " + e.getMessage());
        }
    }

    private static void usrById(Scanner con, UserService userService) {
        try {
            System.out.println("Enter id: ");
            Long id = con.nextLong();
            con.nextLine();

            User user = userService.findUserById(id);
            if (user != null) {
                System.out.println("user - " + user);
                logger.debug("Found user with ID {}: {}", id, user);
            } else {
                System.out.println("user " + id + " not found");
                logger.warn("User with ID {} not found", id);

            }
        } catch (Exception e) {
            logger.error("Error finding user by ID: ", e);
            System.out.println("user not found: " + e.getMessage());
        }
    }

    private static void updateUsr(Scanner con, UserService userService) {
        try {
            System.out.print("User Id to update: ");
            Long id = con.nextLong();
            con.nextLine();

            User existingUser = userService.findUserById(id);
            if (existingUser == null) {
                System.out.println("user " + id + " not found");
                logger.warn("User with ID {} not found for update", id);
                return;
            }

            System.out.print("Enter new name (current: " + existingUser.getName() + "): ");
            String name = con.nextLine();

            System.out.print("Enter new email (current: " + existingUser.getEmail() + "): ");
            String email = con.nextLine();

            System.out.print("Enter new age (current: " + existingUser.getAge() + "): ");
            int age = con.nextInt();
            con.nextLine();

            User updatedUser = new User(id, age, name, email, existingUser.getCreated_at());
            userService.updateUser(id, updatedUser);
            System.out.println("User updated: " + updatedUser);
            logger.info("User {} updated successfully", id);
        } catch (Exception e) {
            logger.error("Error updating user: ", e);
            System.out.println("User update error: " + e.getMessage());
        }
    }

    private static void deleteUsr(Scanner con, UserService userService) {
        try {
            System.out.print("User id to delete: ");
            Long id = con.nextLong();
            con.nextLine();
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                System.out.println("User with id " + id + " deleted");
            } else {
                System.out.println("User with id " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Error deleting user: ", e);
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
}