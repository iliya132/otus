package ru.otus.dao;

import ru.otus.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserDao implements UserDao {
    Map<Long, User> userCache = new HashMap<>();

    public InMemoryUserDao() {
        userCache.put(0L, new User(0L, "test-user", "root", "admin"));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userCache.values().stream().filter(it -> it.getLogin().equals(login)).findFirst();
    }
}
