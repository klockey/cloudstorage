package com.udacity.jwdnd.course1.cloudstorage.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/logout-success")
public class LogoutSuccessController {

    @RequestMapping("/logout-success")
    public String redirect(Model model, RedirectAttributes redirectAttributes) {
       System.out.println("redirect");
      // return "redirect:login?logout";
      //  model.addAttribute("logout-error","");
        redirectAttributes.addAttribute("error","");
        return "redirect: /login";
   }
}

