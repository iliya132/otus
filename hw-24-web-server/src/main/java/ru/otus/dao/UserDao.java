package ru.otus.dao;

import ru.otus.models.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByLogin(String login);
}
