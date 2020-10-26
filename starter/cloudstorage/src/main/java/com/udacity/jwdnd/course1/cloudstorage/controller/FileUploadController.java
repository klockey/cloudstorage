package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Users;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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




//    @GetMapping("/file/view/{fileId}")
//    public  ResponseEntity<ByteArrayResource> getView(Authentication auth, @PathVariable(value="fileId") Integer fileId, Model model,RedirectAttributes redirectAttributes){
//        System.out.println("file view");
//        //Users userDb = userService.getUser(auth.getName());
//        ByteArrayResource resource = null;
//        Files file = fileService.getFileById(fileId);
//        HttpHeaders headers = new HttpHeaders();
//        System.out.println("file.getFileName()");
//        redirectAttributes.addAttribute("download",true);
//
//        try {
//            resource = new ByteArrayResource(file.getFileData());
//            redirectAttributes.addAttribute("download",true);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        return  ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(file.getContentType()))
//                .header(file.getFileName())
//                .body(resource);
//    }


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
        try {
            System.out.println("fileUpload");
            Users userDb = userService.getUser(auth.getName());

            Files file = new Files(null, fileUpload.getOriginalFilename(),fileUpload.getContentType(),Long.toString(fileUpload.getSize()), userDb.getUserId(),fileUpload.getBytes());

            System.out.println("filedata " + fileUpload.getSize());

            if (fileUpload.getSize() > 0){
               fileService.uploadFile(file);
               redirectAttributes.addAttribute("upload",true);
            }else{
                redirectAttributes.addAttribute("selectFile",true);
            }

            for (Files f : this.fileService.getFiles(userDb.getUserId())){
                System.out.println(f.getFileName());
            }
            model.addAttribute("files",this.fileService.getFiles(userDb.getUserId()));
            model.addAttribute("notes",this.noteService.getNotes(userDb.getUserId()));
            model.addAttribute("credentials",this.credentialService.getCredentials(userDb.getUserId()));
           // System.out.println(model.getAttribute("files"));

        }catch (IOException e) {
            e.printStackTrace();
        }
        return  "redirect:/home";
    }
}