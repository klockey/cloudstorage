package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.HashService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;
    private final HashService hashService;

    public CredentialController(CredentialService credentialService, UserService userService, HashService hashService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.hashService = hashService;
    }

    @GetMapping("/credentialModal")
    public String getView(){
        return "redirect:/home";
    }

    @PostMapping("/credentialModal")
    public String postView(Authentication auth, @RequestParam("credentialId")  int credentialId, @RequestParam("url") String url, @RequestParam("username") String username,  @RequestParam("password") String password,  Model model) {
        Users userDb = userService.getUser(auth.getName());
        System.out.println("CREDENTIAL URL:" + url);

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String key = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(password, key);
        System.out.println("CREDENTIAL URL:" + url);
        System.out.println("username " + username);
        System.out.println("key " +key);
        System.out.println("password " + password);
        System.out.println("user id:" + userDb.getUserId());
        System.out.println("credentialid:" + credentialId);
        if (credentialId == 0) {
            credentialService.uploadCredential(url, username, key, hashedPassword, userDb.getUserId());
        }else{
            credentialService.updateCredential(url,username,key,hashedPassword,credentialId);
        }
        model.addAttribute("credentials", this.credentialService.getCredentials(userDb.getUserId()));
        return "redirect:/home";
    }
}


