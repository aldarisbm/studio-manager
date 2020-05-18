package com.jberrio.studiomanager.service;

import com.jberrio.studiomanager.model.User;

public interface UserService {

    public User findUserByEmail(String Email);

    public void saveUser(User user);

    public boolean isAdmin(User user);
}
