package com.epam.app.service;

import com.epam.app.dao.UserDao;
import com.epam.app.model.User;

import java.util.List;

public class UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User retrieveUserById(long userId) {
        return userDao.findUserById(userId);
    }

    public User retrieveUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    public List<User> retrieveUserByName(String name) {
        return userDao.findUserByName(name);
    }

    public User addUser(User user) {
        return userDao.insertUser(user);
    }

    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    public boolean removeUser(long userId) {
        return userDao.deleteUser(userId);
    }
}
