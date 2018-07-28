package com.jberrio.studiomanager.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="calendar")
public class CalendarController {

    @RequestMapping(value="")
    public ModelAndView calendar(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("calendar/agenda-views");
        return modelAndView;
    }
}
