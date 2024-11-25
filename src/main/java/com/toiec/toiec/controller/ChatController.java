package com.toiec.toiec.controller;

import com.toiec.toiec.dto.ResponseGeneral;
import com.toiec.toiec.dto.request.chat.ChatBotRequest;
import com.toiec.toiec.dto.request.chat.ChatRequest;
import com.toiec.toiec.dto.response.chat.ChatResponse;
import com.toiec.toiec.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {

    private final ChatService chatService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/chat")
    public ResponseEntity<?> chatWithGpt(@RequestBody ChatBotRequest mess, Principal principal) {
        return new ResponseEntity<>(
                ResponseGeneral.ofSuccess(chatService.chatWithGpt(mess,principal.getName())
        ), HttpStatus.OK);
    }
}
