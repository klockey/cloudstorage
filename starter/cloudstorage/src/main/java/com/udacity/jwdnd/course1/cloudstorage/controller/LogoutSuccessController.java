package com.udacity.jwdnd.course1.cloudstorage.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout-success")
public class LogoutSuccessController {

    @RequestMapping("/logout-success")
    public String redirect() {
       System.out.println("redirect");
       return "redirect:login?logout";
   }
}

