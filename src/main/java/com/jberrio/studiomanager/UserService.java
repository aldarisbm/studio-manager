package com.jberrio.studiomanager;

import com.jberrio.studiomanager.models.User;

public interface UserService {

    public User findUserByEmail(String Email);
    public void saveUser(User user);
}
