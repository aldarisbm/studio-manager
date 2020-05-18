package com.jberrio.studiomanager.service;

import com.jberrio.studiomanager.model.Role;
import com.jberrio.studiomanager.model.User;
import com.jberrio.studiomanager.repository.RoleDao;
import com.jberrio.studiomanager.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleDao.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userDao.save(user);
    }

    @Override
    public boolean isAdmin(User user) {
        for (Role role : user.getRoles()) {
            if (role.getId() == 1) {
                return true;
            }
        }
        return false;
    }
}