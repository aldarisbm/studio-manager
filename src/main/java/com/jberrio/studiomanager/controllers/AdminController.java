package com.jberrio.studiomanager.controllers;


import com.jberrio.studiomanager.UserService;
import com.jberrio.studiomanager.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="console", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
//        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.addObject("jumbo","Welcome Admin");
        modelAndView.setViewName("admin/console");

        return modelAndView;
    }
}
