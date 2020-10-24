package com.udacity.jwdnd.course1.cloudstorage.service;


import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;
    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }
    public void uploadNote(String noteTitle, String noteDescription, int userId) {
        noteMapper.insert(noteTitle, noteDescription, userId);
    }
    public void updateNote(String noteTitle, String noteDescription, int noteId){
        noteMapper.update(noteTitle, noteDescription, noteId);
    }
    public boolean deleteNote(Integer noteId) {
        noteMapper.delete(noteId);
        return true;
    }
    public List<Notes> getNotes(int userId) {
        return noteMapper.getNotes(userId);
    }
}


