package com.jberrio.studiomanager.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="calendar")
public class CalendarController {

    @RequestMapping(value="")
    public ModelAndView calendar(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("calendar/agenda-views");
        modelAndView.addObject("jumbo","My Calendar");
        return modelAndView;
    }

    @RequestMapping(value="schedule")
    public ModelAndView eventForm(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("jumbo","Schedule");
        modelAndView.setViewName("calendar/eventform");


        return modelAndView;
    }

    @RequestMapping(value="schedule",method=RequestMethod.POST)
    @ResponseBody
    public String datePicker(@RequestParam String startDate,String endDate) {
            return startDate+" "+endDate;
    }
}
