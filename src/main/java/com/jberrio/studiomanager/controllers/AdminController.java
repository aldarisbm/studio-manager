package com.jberrio.studiomanager.controllers;


import com.jberrio.studiomanager.UserService;
import com.jberrio.studiomanager.models.User;
import com.jberrio.studiomanager.models.data.EventDao;
import com.jberrio.studiomanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(value = "admin")
public class AdminController {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    //    GIVES A LIST ALL OF ADMINS AND USERS
    @RequestMapping(value = "console", method = RequestMethod.GET)
    public ModelAndView getConsole() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<User> admins = new LinkedList<>();
        List<User> users = new LinkedList<>();

        for (User aUser : userDao.findAll()) {
            if (userService.isAdmin(aUser)) {
                admins.add(aUser);
            } else {
                users.add(aUser);
            }
        }


        modelAndView.addObject("events", eventDao.findAll());

        modelAndView.addObject("admins", admins);
        modelAndView.addObject("users", users);

//        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.addObject("jumbo", "Welcome Admin");
        modelAndView.setViewName("admin/console");

        return modelAndView;
    }

//    PROCESSES NEW DEMOTIONS OR PROMOTIONS TO USERS

//    @RequestMapping(value="console", method=RequestMethod.POST)
//    public ModelAndView postConsole(){
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByEmail(auth.getName());
//
//
//        modelAndView.setViewName("admin/console");
//        return modelAndView;
//    }


}
