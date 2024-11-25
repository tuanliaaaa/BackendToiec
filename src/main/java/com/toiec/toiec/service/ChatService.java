package com.toiec.toiec.service;

import com.toiec.toiec.dto.request.chat.ChatBotRequest;
import com.toiec.toiec.dto.response.chat.ChatGptResponse;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
     ChatGptResponse chatWithGpt(ChatBotRequest message, String username);
}
