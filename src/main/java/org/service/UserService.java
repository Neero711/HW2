package org.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.model.User;
import org.repository.UserRepository;

import java.util.List;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
        logger.info("UserService init");
    }


    public User createUser(User user) {
        logger.debug("Creating new user: {}", user);
        return repository.create(user);
    }

    public List<User> findAllUsers() {
        logger.debug("Retrieving all users");
        return repository.findAll();
    }

    public User findUserById(Long id) {
        logger.debug("Retrieving user by ID: {}", id);
        return repository.findById(id);
    }

    public User updateUser(Long id, User updUser) {
        logger.debug("Updating user with ID: {}, new data: {}", id, updUser);
        return repository.update(id, updUser);
    }

    public boolean deleteUser   (Long id) {
        logger.debug("Deleting user with ID: {}", id);
        return repository.delete(id);
    }
}
