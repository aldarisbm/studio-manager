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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
        modelAndView.addObject("jumbo", "My Calendar");
        modelAndView.addObject("json",writeToJsonString());
        modelAndView.setViewName("calendar/agenda-views");
        return modelAndView;
    }

    @RequestMapping(value = "schedule")
    public ModelAndView eventForm() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<User> instructors = userDao.findAll();

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
    @ResponseBody
    public String dateChooser(@Valid @ModelAttribute Event event) {

        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Optional<User> instructor = userDao.findById(event.getInstructorId());

        event.setTitle("I: " + instructor.get().getName() + "/ S: "
                + user.getName() + " R:");

        event.setStart(event.getDate() + "T" + event.getStart() + ":00");
        event.setEnd(event.getDate() + "T" + event.getEnd() + ":00");
        event.setUser(user);
        eventDao.save(event);

        String json = event.formatEventToJson(event);

        return json;

    }

    public String writeToJsonString(){
        String json = "";
        try {

            ListIterator<Event> iterator = eventDao.findAll().listIterator();
            json += "[";

            while(iterator.hasNext()) {
                Event iEvent = iterator.next();
                if(!iterator.hasNext()){
                    json += iEvent.formatEventToJson(iEvent);
                }
                else {
                    json += iEvent.formatEventToJson(iEvent) + ",";
                }
            }
            json += "]";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }



//    Optional<User> instructor = userDao.findById(event.getInstructorId());
//    Optional<User> student = userDao.findById(event.getStudentId());
//
//    String theTitle = "Instructor: "+ instructor.get().getName() + " Student: "
//            +student.get().getName()+" Room:";
//    String theStart = event.getDate()+"T"+event.getStartTime()+":00";
//    String theEnd = event.getDate()+"T"+event.getEndTime()+":00";


}
