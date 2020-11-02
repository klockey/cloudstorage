package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Set;
import java.util.HashSet;

@Controller
public class FileUploadController {
    private final FileService fileService;
    private final UserService userService;
    private final CredentialService credentialService;
    private final NoteService noteService;
    private final List<Files> fileUploadList;
    public FileUploadController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        fileUploadList =new ArrayList<Files>();
    }


    @GetMapping("/file/delete/{fileId}")
    public String deleteFile(Authentication auth, @PathVariable(value="fileId") Integer fileId, Model model, RedirectAttributes redirectAttributes){
        System.out.println("delete");
        this.fileService.deleteFile(fileId);
        Users userDb = userService.getUser(auth.getName());
        model.addAttribute("files",this.fileService.getFiles(userDb.getUserId()));
        model.addAttribute("notes",this.noteService.getNotes(userDb.getUserId()));
        model.addAttribute("credentials",this.credentialService.getCredentials(userDb.getUserId()));
        redirectAttributes.addAttribute("deleteFile",true);
        return  "redirect:/home";
    }

    @PostMapping("/fileUpload")
    public String uploader(Authentication auth, @RequestParam("fileToBeUploaded")  MultipartFile fileUpload, Model model, RedirectAttributes redirectAttributes ) {

        boolean duplicateName = false;
        try {
            System.out.println("fileUpload");
            Users userDb = userService.getUser(auth.getName());
            Files file = new Files(null, fileUpload.getOriginalFilename(),fileUpload.getContentType(),Long.toString(fileUpload.getSize()), userDb.getUserId(),fileUpload.getBytes());
            List<Files> sourceList = this.fileService.getFiles(userDb.getUserId());
            Set<String> set = new HashSet<>();

            for (Files files : sourceList){
                set.add(files.getFileName());
            }
            System.out.print(set.size());

            if(set.size() >= 1 && !set.add(file.getFileName())){
                redirectAttributes.addAttribute("duplicateUploadFile",true);
                System.out.println("duplicate");
            } else if (fileUpload.getSize() > 0) {
                fileService.uploadFile(file);
                redirectAttributes.addAttribute("uploadFile",true);
            } else {
                redirectAttributes.addAttribute("selectFile",true);
            }

            model.addAttribute("files",this.fileService.getFiles(userDb.getUserId()));
            model.addAttribute("notes",this.noteService.getNotes(userDb.getUserId()));
            model.addAttribute("credentials",this.credentialService.getCredentials(userDb.getUserId()));

        }catch (IOException e) {
            e.printStackTrace();
        }
        return  "redirect:/home";
    }
}