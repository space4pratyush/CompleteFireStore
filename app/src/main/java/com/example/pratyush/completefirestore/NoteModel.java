package com.example.pratyush.completefirestore;

import com.google.firebase.firestore.Exclude;

public class NoteModel {
    private String documentId;
    private String title;
    private String description;

    public NoteModel() {
//        This constructor is needed
    }

    public NoteModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
