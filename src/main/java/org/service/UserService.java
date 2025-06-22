package org.service;

import org.model.User;
import org.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }


    public User create(User user) {
        return repository.create(user);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id);
    }

    public User update(Long id, User updUser) {
        return repository.update(id, updUser);
    }

    public boolean delete(Long id) {
        return repository.delete(id);
    }
}
