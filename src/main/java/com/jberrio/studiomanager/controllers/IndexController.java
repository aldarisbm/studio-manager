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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EventDao eventDao;

    @RequestMapping(value={"/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("jumbo","Log In");
        modelAndView.setViewName("index/login");
        return modelAndView;
    }


    @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("jumbo","Register");
        modelAndView.addObject("user", user);
        modelAndView.setViewName("index/register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("index/register");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("jumbo","Welcome");
            modelAndView.setViewName("index/welcome");
        }
        return modelAndView;
    }

//    @RequestMapping(value="admin", method = RequestMethod.GET)
//    public ModelAndView home(){
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByEmail(auth.getName());
//        modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
//        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
//        modelAndView.addObject("jumbo","Welcome");
//        modelAndView.setViewName("welcome");
//        return modelAndView;
//    }

    @GetMapping(value="contactus")
    public ModelAndView contactUs(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("jumbo","Contact Us");
        modelAndView.setViewName("index/contactus");
        return modelAndView;
    }

    @GetMapping(value="aboutus")
    public ModelAndView aboutUs(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("jumbo","About Us");
        modelAndView.setViewName("index/aboutus");
        return modelAndView;
    }

    @GetMapping(value="instructors")
    public ModelAndView ourInstructors(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("jumbo","Instructors");
        modelAndView.setViewName("index/instructors");
        return modelAndView;
    }

    @GetMapping(value="")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if(userService.isAdmin(user)){
//            modelAndView.addObject("jumbo","Welcome Admin");
//            List<User> users = userDao.findAll();
//            modelAndView.addObject("users", users);
            modelAndView.setViewName("redirect:admin/console");
            return modelAndView;
        } else {
            List<Event> loggedInEvents = new ArrayList<>();

            for(Event event : eventDao.findAll()){
                if(event.getUser().getId()==user.getId()){
                    loggedInEvents.add(event);
                }
            }

            modelAndView.addObject("events", loggedInEvents);
            modelAndView.addObject("jumbo", "Welcome To Our Studio Manager");
            modelAndView.setViewName("index/index");
            return modelAndView;
        }
    }
}