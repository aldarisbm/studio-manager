package com.jberrio.studiomanager.controllers;

import com.jberrio.studiomanager.models.data.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    public ModelAndView index() throws ParseException {
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
            List<Event> pastLoggedInEvents = new ArrayList<>();
            List<Event> futureLoggedInEvents = new ArrayList<>();

            for(Event event : eventDao.findAll()){
                if(event.getUser().getId()==user.getId()){
                    //formats todays date to the same format as the event date to be able to compare

                    Date eventDate = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                    LocalDateTime ldt = LocalDateTime.now();
                    DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                    String formatter = formmat1.format(ldt);
                    Date todaysDate = new SimpleDateFormat("yyyy-MM-dd").parse(formatter);

                    if(eventDate.compareTo(todaysDate) < 0){
                        pastLoggedInEvents.add(event);
                    } else {
                        futureLoggedInEvents.add(event);
                    }
                }
            }

            modelAndView.addObject("pastEvents", pastLoggedInEvents);
            modelAndView.addObject("futureEvents", futureLoggedInEvents);
            modelAndView.addObject("jumbo", "Welcome To Our Studio Manager");
            modelAndView.setViewName("index/index");
            return modelAndView;
        }
    }

    @GetMapping(value="setbiography")
    public ModelAndView setBio(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if(user.getIsInstructor() == 1) {
            modelAndView.addObject("user", user);
            modelAndView.setViewName("index/setbio");
            return modelAndView;
        } else{
            modelAndView.setViewName("redirect:");
            return modelAndView;
        }
    }

    @RequestMapping(value="setbiography", method=RequestMethod.POST)
    public ModelAndView postBio(@RequestParam String bio){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        user.setBiography(bio);
        userDao.save(user);
        modelAndView.setViewName("redirect:");
        return modelAndView;
    }


}