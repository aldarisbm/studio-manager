package com.jberrio.studiomanager.controllers;


import com.jberrio.studiomanager.models.User;
import com.jberrio.studiomanager.models.data.UserDao;
import com.jberrio.studiomanager.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping(value ="")
public class IndexController {

    @Autowired
    UserDao userDao;

    @RequestMapping(value = "")
    public String indexPage(Model model){
        model.addAttribute("title","Index");
        return "index";
    }

    @RequestMapping(value = "register", method=RequestMethod.GET)
    public String registration(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value = "register", method=RequestMethod.POST)
    public String registrationPost(Model model, @RequestParam int key, @RequestParam String verify, @Valid @ModelAttribute User user, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("title","PLACEHOLDER");
            return "register";
        } else if(!user.getPassword().equals(verify)) {
            return "register";
        } else {
            for(User aUser : userDao.findAll()){
                if(aUser.getName().equals(user.getName())){
                    return "register";
                }
            }

            for(User aUser : userDao.findAll()){
                if(aUser.getEmail().equals(user.getEmail())){
                    return "register";
                }
            }

            if(key == 2299123){
                String thePassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
                user.setPassword(thePassword);
                user.setLevel(2);
                userDao.save(user);
                return "welcome";
            }else{
                String thePassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
                user.setLevel(1);
                user.setPassword(thePassword);
                userDao.save(user);
                return "redirect:welcome";
            }
        }

    }

    @RequestMapping(value = "welcome")
    public String welcome(){
        return "welcome";
    }


    @RequestMapping(value = "login")
    public String login(){
        return "login";
    }
}
