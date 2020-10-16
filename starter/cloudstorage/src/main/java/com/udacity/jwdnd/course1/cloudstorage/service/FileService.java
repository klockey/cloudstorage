package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public Integer uploadFile(Files file) {
        return fileMapper.saveFile(new Files(file.getFileId(), file.getFileName(), file.getContentType(), file.getFileSize(), file.getUserId(), file.getFileData()));
    }

    public void deleteFile(Integer fileId) {
        fileMapper.deleteFileByFileId(fileId);
    }

    public List<Files> getFiles(int userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public Files getFileById(Integer fileId) { return fileMapper.getFileByFileId(fileId); }

}