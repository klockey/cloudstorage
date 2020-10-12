package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileUploadController {

    private final FileService fileService;
    private final UserService userService;
    private List<Files> fileUploadList;

    public FileUploadController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
        fileUploadList =new ArrayList<Files>();
    }

    @GetMapping("/delete-file/{fileId}")
    public String deleteFile(Authentication auth, @PathVariable(value="fileId") Integer fileId, Model model){
        this.fileService.deleteFile(fileId);
        Users userDb = userService.getUser(auth.getName());
        model.addAttribute("fileUploads",this.fileService.getFiles(userDb.getUserId()));
        return "redirect:/home";
    }

//    public String deleteNote(Authentication auth, @PathVariable(value = "noteId") Integer noteId, Model model) {
//        this.noteService.deleteNote(noteId);
//        Users userDb = userService.getUser(auth.getName());
//        model.addAttribute("notes", this.noteService.getNotes(userDb.getUserId()));
//        return "redirect:/home";
//    }

    @GetMapping("/fileUpload")
    public String preview(){
        return "redirect:/home";
    }

     @PostMapping("/fileUpload")
     public String uploader(Authentication auth, @RequestParam("fileToBeUploaded")  MultipartFile fileUpload, Model model ) {
        try {
            System.out.println("fileUpload");
            Users userDb = userService.getUser(auth.getName());
            Files file = new Files(null, fileUpload.getOriginalFilename(),fileUpload.getContentType(),Long.toString(fileUpload.getSize()), userDb.getUserId(),fileUpload.getBytes());
//            fileUploadList.add(file);
            fileService.uploadFile(file);
            for (Files f : this.fileService.getFiles(userDb.getUserId())){
                System.out.println(f.getFileName());
            }

            model.addAttribute("fileUploads", this.fileService.getFiles(userDb.getUserId()));
        }catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/home";
     }
}
