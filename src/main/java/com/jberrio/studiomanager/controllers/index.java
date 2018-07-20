package com.jberrio.studiomanager.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value ="")
public class index {
    @RequestMapping(value = "")
    public String indexPage(){
        return "index";
    }
}
