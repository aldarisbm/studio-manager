package com.jberrio.studiomanager.controller;


import com.jberrio.studiomanager.service.UserService;
import com.jberrio.studiomanager.model.Event;
import com.jberrio.studiomanager.model.Role;
import com.jberrio.studiomanager.model.User;
import com.jberrio.studiomanager.repository.EventDao;
import com.jberrio.studiomanager.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping(value = "/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    //    GIVES A LIST ALL OF ADMINS AND USERS
    @GetMapping("/console")
    public ModelAndView getConsole() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return allInfoAdmin();
    }

//    PROCESSES NEW DEMOTIONS OR PROMOTIONS TO USERS

    @PostMapping(value = "/demote")
    public ModelAndView demote(@RequestParam int id) throws ParseException {
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

    @PostMapping(value = "/promote")
    public ModelAndView promote(@RequestParam int id) throws ParseException {
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

    @PostMapping(value = "/deletepast")
    public ModelAndView deletePast(@RequestParam int id) {
        Optional<Event> deletedEvent = eventDao.findById(id);

        eventDao.delete(deletedEvent.get());

        return new ModelAndView("redirect:console");
    }


    @PostMapping(value = "/deletefuture")
    public ModelAndView deleteFuture(@RequestParam int id) {
        Optional<Event> deletedEvent = eventDao.findById(id);

        eventDao.delete(deletedEvent.get());

        return new ModelAndView("redirect:console");
    }

    @PostMapping(value = "makestudent")
    public ModelAndView makeStudent(@RequestParam int id) throws ParseException {
        Optional<User> newInstructor = userDao.findById(id);

        newInstructor.get().setIsInstructor(0);

        userDao.save(newInstructor.get());

        return new ModelAndView("redirect:console");
    }

    @PostMapping(value = "/makeinstructor")
    public ModelAndView makeInstructor(@RequestParam int id) throws ParseException {
        Optional<User> newInstructor = userDao.findById(id);

        newInstructor.get().setIsInstructor(1);

        userDao.save(newInstructor.get());

        return new ModelAndView("redirect:console");
    }

    @GetMapping(value = "/index")
    public ModelAndView index() throws ParseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<Event> pastLoggedInEvents = new ArrayList<>();
        List<Event> futureLoggedInEvents = new ArrayList<>();
        ModelAndView modelAndView = new ModelAndView();

        for (Event event : eventDao.findByIsActive(1)) {
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


    //SEEDS THE CALENDAR WITH RANDOM EVENTS

    @GetMapping(value = "/seed")
    public ModelAndView seed() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:/calendar");
        Random rand = new Random();
        for (int i = 0; i < 500; i++) {
            Event aRandomEvent = new Event();
            int randomInstructor = rand.nextInt(userDao.findAll().size()) + 1;
            int randomUser = rand.nextInt(userDao.findAll().size()) + 1;
            while (randomUser == randomInstructor) {
                randomUser = rand.nextInt(userDao.findAll().size()) + 1;
            }

            aRandomEvent.setInstructorId(randomInstructor);
            aRandomEvent.setUser(userDao.findById(randomUser).get());
            String year = "2018";

            Integer daySeed = rand.nextInt(28);
            Integer monthSeed = rand.nextInt(12);

            Integer endTimeSeed = rand.nextInt();

            String randomDay = "";
            String randomMonth = "";

            if (daySeed < 10) {
                randomDay = "0" + Integer.toString(daySeed);
            } else {
                randomDay = Integer.toString(daySeed);
            }

            if (monthSeed < 10) {
                randomMonth = "0" + Integer.toString(monthSeed);
            } else {
                randomMonth = Integer.toString(monthSeed);
            }

            String hour = Integer.toString(rand.nextInt(12));
            List<String> minutes = Arrays.asList("00", "15", "30", "45");

            aRandomEvent.setDate(year + "-" + randomMonth + "-" + randomDay);
            aRandomEvent.setRoom("Studio 1");

            aRandomEvent.setTitle("I: " + userDao.getOne(aRandomEvent.getInstructorId()).getName() + " | S: "
                    + aRandomEvent.getUser().getName() + " | R: " + aRandomEvent.getRoom());


            aRandomEvent.setStart(aRandomEvent.getDate() + "T" + randomMonth + ":" + minutes.get(rand.nextInt(4)) + ":00");

            aRandomEvent.setEnd(aRandomEvent.getDate() + "T" + Integer.toString(rand.nextInt(12) + 12) + ":" + minutes.get(rand.nextInt(4)) + ":00");

            aRandomEvent.setColor(aRandomEvent.getUser().getColor());
            aRandomEvent.setIsActive(1);
            eventDao.save(aRandomEvent);
        }

        return modelAndView;
    }


    //PROCESSES ALL OF THE ADMIN NEEDED INFO

    private ModelAndView allInfoAdmin() throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("id", user.getId());

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

        modelAndView.addObject("jumbo", "Welcome Admin");
        modelAndView.setViewName("admin/console");

        return modelAndView;
    }

}
