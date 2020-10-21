package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;@Controller
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;
    private final HashService hashService;
    private final EncryptionService encryptionService;
    private final FileService fileService;
    private final NoteService noteService;
    public CredentialController(CredentialService credentialService, EncryptionService encryptionService, UserService userService, HashService hashService, NoteService noteService, FileService fileService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.hashService = hashService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.noteService = noteService;
    }
    @GetMapping("/credential/delete/{credentialId}")
    public String deleteCredential(Authentication auth, @PathVariable(value="credentialId") int credentialId, Model model){
        System.out.println("delete " + credentialId);
        Users userDb = userService.getUser(auth.getName());
        this.credentialService.deleteCredential(credentialId);
        model.addAttribute("notes", this.noteService.getNotes(userDb.getUserId()));
        model.addAttribute("files", this.fileService.getFiles(userDb.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentials(userDb.getUserId()));
       // return  "home";
        return  "redirect:/home";
    }
    @PostMapping("/credentialModal")
    public String postView(Authentication auth, @ModelAttribute Credentials credentials, @RequestParam("credentialId")  int credentialId, @RequestParam("url") String url, @RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        Users userDb = userService.getUser(auth.getName());
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        if (credentialId == 0) {
            credentialService.uploadCredential(url, username, encodedKey,encryptedPassword, userDb.getUserId());
        }else{
            credentialService.updateCredential(url,username,encodedKey,encryptedPassword,credentialId);
        }
        model.addAttribute("credentials", this.credentialService.getCredentials(userDb.getUserId()));
        model.addAttribute("notes", this.noteService.getNotes(userDb.getUserId()));
        model.addAttribute("files", this.fileService.getFiles(userDb.getUserId()));
        return  "redirect:/home";
    }
}