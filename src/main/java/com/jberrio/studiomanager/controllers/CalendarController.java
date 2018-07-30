package com.jberrio.studiomanager.controllers;


import com.jberrio.studiomanager.UserService;
import com.jberrio.studiomanager.models.User;
import com.jberrio.studiomanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value="calendar")
public class CalendarController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    @RequestMapping(value="")
    public ModelAndView calendar(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.setViewName("calendar/agenda-views");
        modelAndView.addObject("jumbo","My Calendar");
        return modelAndView;
    }

    @RequestMapping(value="schedule")
    public ModelAndView eventForm(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<User> instructors = userDao.findAll();
        modelAndView.addObject("instructors",instructors);
        modelAndView.addObject("user",user);
        modelAndView.addObject("jumbo","Schedule");
        modelAndView.setViewName("calendar/eventform");


        return modelAndView;
    }

    @RequestMapping(value="schedule",method=RequestMethod.POST)
    @ResponseBody
    public String datePicker(@RequestParam String startTime,String endTime, String date, String room, String instructor) {

            return " Lesson with " + instructor + " on " + date +" at "+ room + " starting @ " + startTime + " and ending @ " + endTime;

    }
}
