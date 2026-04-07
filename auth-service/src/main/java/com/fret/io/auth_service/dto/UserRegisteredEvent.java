package com.fret.io.auth_service.dto;

import com.fret.io.auth_service.model.DocumentType;

import java.util.UUID;

public class UserRegisteredEvent {

    private UUID idUser;
    private String email;
    private DocumentType documentType;
    private String document;

    public UserRegisteredEvent(UUID idUser, String email, DocumentType documentType, String document) {
        this.idUser = idUser;
        this.email = email;
        this.documentType = documentType;
        this.document = document;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
