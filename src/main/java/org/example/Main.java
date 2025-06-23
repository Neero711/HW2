package org.example;

import org.dao.UserDao;
import org.model.User;
import org.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.util.HibernateUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting User CRUD application with Hibernate");
            UserDao userDao = new UserDao();
            UserService userService = new UserService(userDao);
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
                            HibernateUtil.shutdown();
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
        } catch (Exception e) {
            logger.error("Application error: ", e);
            System.out.println("Critical error: " + e.getMessage());
        } finally {
            HibernateUtil.shutdown();
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
            List<User> users = userService.getAllUsers();
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
            System.out.print("Enter user ID: ");
            long id = con.nextLong();
            con.nextLine();

            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                System.out.println("User found: " + user.get());
            } else {
                System.out.println("User with ID " + id + " not found.");
            }
        } catch (Exception e) {
            logger.error("Error finding user: ", e);
            System.out.println("Error finding user: " + e.getMessage());
        }
    }

    private static void updateUsr(Scanner con, UserService userService) {
        try {
            System.out.print("Enter user ID to update: ");
            long id = con.nextLong();
            con.nextLine();

            Optional<User> existingUser = userService.getUserById(id);
            if (existingUser.isEmpty()) {
                System.out.println("User not found.");
                return;
            }

            System.out.print("Enter new name (" + existingUser.get().getName() + "): ");
            String name = con.nextLine();

            System.out.print("Enter new email (" + existingUser.get().getEmail() + "): ");
            String email = con.nextLine();

            System.out.print("Enter new age (" + existingUser.get().getAge() + "): ");
            int age = con.nextInt();
            con.nextLine();

            User userToUpdate = new User(id, age, name, email, existingUser.get().getCreated_at());
            User updatedUser = userService.updateUser(userToUpdate);
            System.out.println("User updated: " + updatedUser);
        } catch (Exception e) {
            logger.error("Error updating user: ", e);
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    private static void deleteUsr(Scanner con, UserService userService) {
        try {
            System.out.print("Enter user ID to delete: ");
            long id = con.nextLong();
            con.nextLine();

            userService.deleteUser(id);
            System.out.println("User with ID " + id + " deleted successfully.");
        } catch (Exception e) {
            logger.error("Error deleting user: ", e);
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
}