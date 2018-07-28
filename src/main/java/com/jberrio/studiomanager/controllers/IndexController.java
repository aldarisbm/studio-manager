package com.jberrio.studiomanager.controllers;

import com.jberrio.studiomanager.UserService;
import com.jberrio.studiomanager.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @RequestMapping(value={"/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
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
            modelAndView.setViewName("register");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("register");

        }
        return modelAndView;
    }

    @RequestMapping(value="welcome", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("welcome");
        return modelAndView;
    }

    @GetMapping(value="contactus")
    public ModelAndView contactUs(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.setViewName("contactus");
        return modelAndView;
    }

    @GetMapping(value="aboutus")
    public ModelAndView aboutUs(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.setViewName("aboutus");
        return modelAndView;
    }

    @GetMapping(value="instructors")
    public ModelAndView ourInstructors(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.setViewName("instructors");
        return modelAndView;
    }
}

//
//
//import com.jberrio.studiomanager.models.User;
//import com.jberrio.studiomanager.models.data.UserDao;
//import com.jberrio.studiomanager.security.BCrypt;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.validation.Valid;
//
//@Controller
//@RequestMapping(value ="")
//public class IndexController {
//
//    @Autowired
//    UserDao userDao;
//
//    @RequestMapping(value = "")
//    public String indexPage(Model model){
//        model.addAttribute("title","Index");
//        return "index";
//    }
//
//    @RequestMapping(value = "register", method=RequestMethod.GET)
//    public String registration(Model model){
//        model.addAttribute("user", new User());
//        return "register";
//    }
//
//    @RequestMapping(value = "register", method=RequestMethod.POST)
//    public String registrationPost(Model model, @RequestParam int key, @RequestParam String verify, @Valid @ModelAttribute User user, Errors errors){
//        if(errors.hasErrors()){
//            model.addAttribute("title","PLACEHOLDER");
//            return "register";
//        } else if(!user.getPassword().equals(verify)) {
//            return "register";
//        } else {
//
//            for(User aUser : userDao.findAll()){
//                if(aUser.getEmail().equals(user.getEmail())){
//                    return "register";
//                }
//            }
//
//            if(key == 2299123){
//                String thePassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
//                user.setPassword(thePassword);
//                user.setLevel(2);
//                userDao.save(user);
//                return "welcome";
//            }else{
//                String thePassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
//                user.setLevel(1);
//                user.setPassword(thePassword);
//                userDao.save(user);
//                return "redirect:welcome";
//            }
//        }
//
//    }
//
//    @RequestMapping(value = "welcome")
//    public String welcome(){
//        return "welcome";
//    }
//
//
//    @RequestMapping(value = "login")
//    public String login(){
//        return "login";
//    }
//}
