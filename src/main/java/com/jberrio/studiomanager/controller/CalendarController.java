package com.jberrio.studiomanager.controller;


import com.jberrio.studiomanager.model.Event;
import com.jberrio.studiomanager.model.User;
import com.jberrio.studiomanager.repository.EventDao;
import com.jberrio.studiomanager.repository.UserDao;
import com.jberrio.studiomanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@Controller
@RequestMapping(value = "calendar")
public class CalendarController {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "")
    public ModelAndView calendar() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("id", user.getId());
        modelAndView.addObject("jumbo", "Calendar");
        modelAndView.addObject("json", writeToJsonString());
        modelAndView.setViewName("calendar/agenda-views");
        return modelAndView;
    }


    @RequestMapping(value = "{id}")
    public ModelAndView calendar(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("id", user.getId());
        modelAndView.addObject("jumbo", userDao.findById(id).get().getName() + "'s Calendar");
        modelAndView.addObject("json", writeToJsonString(id));
        modelAndView.setViewName("calendar/agenda-views");
        return modelAndView;
    }


    @RequestMapping(value = "schedule")
    public ModelAndView eventForm() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<User> instructors = new ArrayList<>();
        modelAndView.addObject("id", user.getId());

        for (User instructor : userDao.findAll()) {
            if (instructor.getIsInstructor() == 1) {
                instructors.add(instructor);
            }
        }

        ListIterator<User> i = instructors.listIterator();

        while (i.hasNext()) {
            User userToRemove = i.next();
            if (userToRemove.equals(user)) {
                i.remove();
            }
        }

        modelAndView.addObject("instructors", instructors);
        modelAndView.addObject("user", user);
        modelAndView.addObject("jumbo", "Schedule");
        modelAndView.setViewName("calendar/eventform");


        return modelAndView;
    }


    @RequestMapping(value = "schedule", method = RequestMethod.POST)
    public ModelAndView dateChooser(@Valid @ModelAttribute Event event) {

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Optional<User> instructor = userDao.findById(event.getInstructorId());

        char lastInitial = instructor.get().getLastName().toUpperCase().charAt(0);

        String successInstructor = "Instructor: " + instructor.get().getName() + " " + lastInitial + '.';
        String sucessDate = "Date: " + event.getDate() +
                ", " + event.getStart() + " to " + event.getEnd();

        event.setTitle("I: " + instructor.get().getName() + " | S: "
                + user.getName() + " | R: " + event.getRoom());

        event.setStart(event.getDate() + "T" + event.getStart() + ":00");
        event.setEnd(event.getDate() + "T" + event.getEnd() + ":00");

        modelAndView.addObject("successInstructor", successInstructor);
        modelAndView.addObject("successDate", sucessDate);
        modelAndView.addObject("id", user.getId());
        modelAndView.setViewName("calendar/success");

        event.setColor(user.getColor());
        event.setUser(user);
        event.setIsActive(0);
        eventDao.save(event);

        return modelAndView;

    }


    public String writeToJsonString() {
        String json = "";
        try {

            ListIterator<Event> iterator = eventDao.findByIsActive(1).listIterator();
            json += "[";

            while (iterator.hasNext()) {
                Event iEvent = iterator.next();
                if (!iterator.hasNext()) {
                    json += iEvent.formatEventToJson(iEvent);
                } else {
                    json += iEvent.formatEventToJson(iEvent) + ",";
                }
            }
            json += "]";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    public String writeToJsonString(int id) {
        String json = "";
        try {

            List<Event> userEvents = new ArrayList<>();
            for (Event event : eventDao.findByIsActive(1)) {
                if (event.getUser().getId() == id || event.getInstructorId() == id) {
                    userEvents.add(event);
                }
            }

            ListIterator<Event> iterator = userEvents.listIterator();
            json += "[";

            while (iterator.hasNext()) {
                Event iEvent = iterator.next();
                if (!iterator.hasNext()) {
                    json += iEvent.formatEventToJson(iEvent);
                } else {
                    json += iEvent.formatEventToJson(iEvent) + ",";
                }
            }
            json += "]";

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(json);
        return json;
    }
}
