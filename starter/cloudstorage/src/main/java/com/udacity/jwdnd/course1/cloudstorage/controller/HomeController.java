package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.AttributedString;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final NoteService noteService;
    private final UserService userService;
    private final FileService fileService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(NoteService noteService, UserService userService, FileService fileService, CredentialService credentialService, EncryptionService encryptionService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }
    @GetMapping
    public String getHome(Authentication auth, Model model, RedirectAttributes redirectAttributes) {
        Users userDb = userService.getUser(auth.getName());
        model.addAttribute("notes", this.noteService.getNotes(userDb.getUserId()));
        model.addAttribute("files", this.fileService.getFiles(userDb.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentials(userDb.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        redirectAttributes.addAttribute("nothing", "true");
        return  "home";
    }
}