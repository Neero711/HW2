package org.repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public User create(User user){
        user.setId(idGen.getAndIncrement());
        user.setCreated_at(LocalDateTime.now());
        users.add(user);
        logger.debug("user with id: {}", user.getId());
        return user;
    }

    public  List<User> findAll(){
        logger.debug("all users, : {}", users.size());
        return new ArrayList<>(users);
    }

    public User findById(Long id) {
        logger.debug("Looking for user with ID: {}", id);
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public User update(Long id, User updUser) {
        logger.debug("updating user with ID: {}", id);
        User user = findById(id);
        if (user != null) {
            user.setName(updUser.getName());
            user.setEmail(updUser.getEmail());
            user.setAge(updUser.getAge());
            logger.info("ID {} updated", id);
            return  user;
        }
        logger.warn("ID {} failed", id);
        return null;
    }

    public boolean delete(Long id){
        logger.debug("Deleting user with ID: {}", id);
        //return users.removeIf(user -> user.getId().equals(id));
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            logger.info("{} deleted", id);
        } else {
            logger.warn(" {}  deletion failed", id);
        }
        return removed;
    }
}
