package com.jberrio.studiomanager.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value ="")
public class index {
    @RequestMapping(value = "")
    public String indexPage(Model model){
        model.addAttribute("title","Index");
        return "index";
    }
    @RequestMapping(value = "register")
    public String registration(){
        return "register";
    }
}
