package com.hackathon.KDT_HACK.dto;


public class ResponseDTO {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseDTO(String message) {
        this.message = message;
    }
}
