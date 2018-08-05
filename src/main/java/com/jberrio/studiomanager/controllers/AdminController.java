package com.jberrio.studiomanager.controllers;


import com.jberrio.studiomanager.models.data.UserService;
import com.jberrio.studiomanager.models.Event;
import com.jberrio.studiomanager.models.Role;
import com.jberrio.studiomanager.models.User;
import com.jberrio.studiomanager.models.data.EventDao;
import com.jberrio.studiomanager.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
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
    public ModelAndView getConsole() throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        return allInfoAdmin();
    }

//    PROCESSES NEW DEMOTIONS OR PROMOTIONS TO USERS

    @RequestMapping(value = "demote", method = RequestMethod.POST)
    public ModelAndView demote(@RequestParam int id) throws ParseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Optional<User> demotedUser = userDao.findById(id);

        for (Role role : demotedUser.get().getRoles()) {
            demotedUser.get().getRoles().remove(role);
        }

        Set<Role> demoted = new HashSet<>();
        demoted.add(new Role(2, "USER"));

        demotedUser.get().setRoles(demoted);
        userDao.save(demotedUser.get());
        return new ModelAndView("redirect:console");
    }

    @RequestMapping(value = "promote", method = RequestMethod.POST)
    public ModelAndView promote(@RequestParam int id) throws ParseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        Optional<User> promotedUser = userDao.findById(id);

        for (Role role : promotedUser.get().getRoles()) {
            promotedUser.get().getRoles().remove(role);
        }

        Set<Role> promoted = new HashSet<>();
        promoted.add(new Role(1, "ADMIN"));

        promotedUser.get().setRoles(promoted);
        userDao.save(promotedUser.get());
        return new ModelAndView("redirect:console");
    }

    @RequestMapping(value = "deletepast", method = RequestMethod.POST)
    public ModelAndView deletePast(@RequestParam int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Optional<Event> deletedEvent = eventDao.findById(id);

        eventDao.delete(deletedEvent.get());

        return new ModelAndView("redirect:console");
    }


    @RequestMapping(value = "deletefuture", method = RequestMethod.POST)
    public ModelAndView deleteFuture(@RequestParam int id) {

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Optional<Event> deletedEvent = eventDao.findById(id);

        eventDao.delete(deletedEvent.get());

        return new ModelAndView("redirect:console");
    }

    @RequestMapping(value = "makestudent", method = RequestMethod.POST)
    public ModelAndView makeStudent(@RequestParam int id) throws ParseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Optional<User> newInstructor = userDao.findById(id);

        newInstructor.get().setIsInstructor(0);

        userDao.save(newInstructor.get());

        return new ModelAndView("redirect:console");
    }

    @RequestMapping(value = "makeinstructor", method = RequestMethod.POST)
    public ModelAndView makeInstructor(@RequestParam int id) throws ParseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Optional<User> newInstructor = userDao.findById(id);

        newInstructor.get().setIsInstructor(1);

        userDao.save(newInstructor.get());

        return new ModelAndView("redirect:console");
    }

    @GetMapping(value = "index")
    public ModelAndView index() throws ParseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        List<Event> pastLoggedInEvents = new ArrayList<>();
        List<Event> futureLoggedInEvents = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView();

        for (Event event : eventDao.findAll()) {
            if (event.getUser().getId() == user.getId()) {
                //formats todays date to the same format as the event date to be able to compare

                Date eventDate = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
                LocalDateTime ldt = LocalDateTime.now();
                DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                String formatter = formmat1.format(ldt);
                Date todaysDate = new SimpleDateFormat("yyyy-MM-dd").parse(formatter);

                if (eventDate.compareTo(todaysDate) < 0) {
                    pastLoggedInEvents.add(event);
                } else {
                    futureLoggedInEvents.add(event);
                }
            }
        }

        modelAndView.addObject("pastEvents", pastLoggedInEvents);
        modelAndView.addObject("futureEvents", futureLoggedInEvents);
        modelAndView.addObject("jumbo", "Welcome To Our Studio Manager");
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }


    @GetMapping(value="seed")
    public ModelAndView seed(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:calendar");
        Random rand = new Random();
        Event aRandomEvent = new Event();
        for(int i = 0; i < 100;i++){
            String year = "2018";
            List<String> AMorPM = Arrays.asList("AM","PM");
            String SECONDS = "00";
            String randomDay = Integer.toString(rand.nextInt(29));
            String randomMonth = Integer.toString(rand.nextInt(13));
            String hour = Integer.toString(rand.nextInt(13));
            List<String> minutes = Arrays.asList("00","15","30","45");



            aRandomEvent.setInstructorId(rand.nextInt(4));
            aRandomEvent.setUser(userDao.findById(rand.nextInt(4)).get());


            eventDao.save(aRandomEvent);
        }

        return modelAndView;
    }



    //PROCESSES ALL OF THE ADMIN NEEDED INFO

    public ModelAndView allInfoAdmin() throws ParseException {

        ModelAndView modelAndView = new ModelAndView();

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

        for (Event event : eventDao.findAll()) {
            //formats todays date to the same format as the event date to be able to compare
            Date eventDate = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDate());
            LocalDateTime ldt = LocalDateTime.now();
            DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            String formatter = formmat1.format(ldt);
            Date todaysDate = new SimpleDateFormat("yyyy-MM-dd").parse(formatter);

            if (eventDate.compareTo(todaysDate) < 0) {
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

        modelAndView.addObject("instructors", instructors);
        modelAndView.addObject("students", students);

        modelAndView.addObject("pastEvents", pastEvents);
        modelAndView.addObject("futureEvents", futureEvents);

        modelAndView.addObject("admins", admins);
        modelAndView.addObject("users", users);

//        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.addObject("jumbo", "Welcome Admin");
        modelAndView.setViewName("admin/console");

        return modelAndView;
    }

}
