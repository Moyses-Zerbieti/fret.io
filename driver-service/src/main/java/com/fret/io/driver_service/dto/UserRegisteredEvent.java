package com.fret.io.driver_service.dto;

import java.util.UUID;

public class UserRegisteredEvent {

    private UUID idUser;
    private String email;
    private String document;
    private String documentType;

    public UserRegisteredEvent() {
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

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}
