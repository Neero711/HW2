package org.service;

import org.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    //private final UserRepository repository;
    private final UserDao userDao;

   /* public UserService(UserRepository repository){
        this.repository = repository;
        logger.info("UserService init");
    } */

   public UserService(UserDao userDao) {
       this.userDao = userDao;
       logger.info("UserService init with UserDao");
   }

    public User createUser(User user) {
        try {
            user.setCreated_at(LocalDateTime.now());
            return userDao.save(user);
        } catch (Exception e) {
            logger.error("Error creating user: {}", user, e);
            throw new ServiceException("Failed to create user", e);
        }
    }

    public List<User> getAllUsers() {
        try {
            return userDao.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving all users", e);
            throw new ServiceException("Failed to retrieve all users", e);
        }
    }

    public Optional<User> getUserById(Long id) {
        try {
            return userDao.findById(id);
        } catch (Exception e) {
            logger.error("Error retrieving user by id: {}", id, e);
            throw new ServiceException("Failed to retrieve user by id: " + id, e);
        }
    }

    public User updateUser(User user) {
        try {
            return userDao.update(user);
        } catch (Exception e) {
            logger.error("Error updating user: {}", user, e);
            throw new ServiceException("Failed to update user", e);
        }
    }
    public void deleteUser(Long id) {
        try {
            userDao.delete(id);
        } catch (Exception e) {
            logger.error("Error deleting user with id: {}", id, e);
            throw new ServiceException("Failed to delete user with id: " + id, e);
        }
    }
}
