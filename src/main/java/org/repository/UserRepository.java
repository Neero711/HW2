package org.repository;

import org.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public User create(User user){
        user.setId(idGen.getAndIncrement());
        user.setCreated_at(LocalDateTime.now());
        users.add(user);
        return user;
    }

    public  List<User> findAll(){
        return new ArrayList<>(users);
    }

    public User findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public User update(Long id, User updUser) {
        User user = findById(id);
        if (user != null) {
            user.setName(updUser.getName());
            user.setEmail(updUser.getEmail());
            user.setAge(updUser.getAge());
            return  user;
        }
        return null;
    }

    public boolean delete(Long id){
        return users.removeIf(user -> user.getId().equals(id));
    }
}
