package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
@Controller
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;
    private final FileService fileService;
    private final CredentialService credentialService;
    public NoteController (NoteService noteService, UserService userService, FileService fileService, CredentialService credentialService) {
        System.out.println("noteservice");
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
    }

    @PostMapping("/noteModal")
    public String postView(Authentication auth, @RequestParam("noteId") Integer noteId, @RequestParam("noteTitle") String noteTitle, @RequestParam("noteDescription") String noteDescription, Model model, RedirectAttributes redirectAttributes)   {
        Users userDb = userService.getUser(auth.getName());
        System.out.println("NOTE ID:" + noteId);
        System.out.println("NOTE TITLE:" + noteTitle);
        System.out.println("NOTE DESCRIPTION:" + noteDescription);
        System.out.println("user id:" + userDb.getUserId());
      //  redirectAttributes.addAttribute("creation",false);

        if(noteId == 0){
            noteService.uploadNote(noteTitle, noteDescription, userDb.getUserId());
    //        model.addAttribute("creation", true);
            System.out.println("creation");
//            redirectAttributes.addAttribute("update","false");
            redirectAttributes.addAttribute("creation","true");
        } else{
            System.out.println("update");
            noteService.updateNote(noteTitle, noteDescription, noteId);
          //  redirectAttributes.addAttribute("creation","false");
            redirectAttributes.addAttribute("update", "true");
         //   redirectAttributes.addAttribute("deletion", false);
        }

        model.addAttribute("notes", this.noteService.getNotes(userDb.getUserId()));
        model.addAttribute("files", this.fileService.getFiles(userDb.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentials(userDb.getUserId()));

        return  "redirect:/home";
    }
    @GetMapping("/delete-note/{noteId}")
    public String deleteNote(Authentication auth, @PathVariable(value = "noteId") Integer noteId, Model model, RedirectAttributes redirectAttributes) {
        this.noteService.deleteNote(noteId);
        Users userDb = userService.getUser(auth.getName());
        model.addAttribute("notes", this.noteService.getNotes(userDb.getUserId()));
        model.addAttribute("files", this.fileService.getFiles(userDb.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentials(userDb.getUserId()));
       // model.addAttribute("deletion", true);

        redirectAttributes.addAttribute("deletion",true);
        return  "redirect:/home";
    }
}