package com.toiec.toiec.dto.request.chat;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRequest {

    private String model;
    private List<Message> messages;


    public ChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }
}
