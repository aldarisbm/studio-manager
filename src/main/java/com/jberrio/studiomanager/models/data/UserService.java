package com.jberrio.studiomanager.models.data;

import com.jberrio.studiomanager.models.User;

public interface UserService {

    public User findUserByEmail(String Email);

    public void saveUser(User user);

    public boolean isAdmin(User user);
}
