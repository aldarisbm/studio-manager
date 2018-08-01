package com.jberrio.studiomanager.controllers;


import com.jberrio.studiomanager.UserService;
import com.jberrio.studiomanager.models.Event;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public ModelAndView getConsole()  throws Exception{


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


        List<Event> pastEvents = new ArrayList<>();
        List<Event> futureEvents = new ArrayList<>();

        for(Event event : eventDao.findAll()){
                //formats todays date to the same format as the event date to be able to compare
                Date eventDate = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                LocalDateTime ldt = LocalDateTime.now();
                DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                String formatter = formmat1.format(ldt);
                Date todaysDate = new SimpleDateFormat("yyyy-MM-dd").parse(formatter);

                if(eventDate.compareTo(todaysDate) < 0){
                    pastEvents.add(event);
                } else {
                    futureEvents.add(event);
                }

        }



        List<User> instructors = new LinkedList<>();
        List<User> students = new LinkedList<>();

        for (User aUser : userDao.findAll()) {
            if (aUser.getIsInstructor() == 1) {
                instructors.add(aUser);
            } else {
                students.add(aUser);
            }
        }

        modelAndView.addObject("instructors",instructors);
        modelAndView.addObject("students",students);

        modelAndView.addObject("pastEvents", pastEvents);
        modelAndView.addObject("futureEvents", futureEvents);

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
