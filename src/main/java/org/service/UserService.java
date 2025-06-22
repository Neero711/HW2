package org.service;

import org.model.User;
import org.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }


    public User createUser(User user) {
        return repository.create(user);
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public User findUserById(Long id) {
        return repository.findById(id);
    }

    public User updateUser(Long id, User updUser) {
        return repository.update(id, updUser);
    }

    public boolean deleteUser   (Long id) {
        return repository.delete(id);
    }
}
