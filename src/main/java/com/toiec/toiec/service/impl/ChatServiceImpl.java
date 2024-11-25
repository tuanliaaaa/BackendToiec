package com.toiec.toiec.service.impl;

import com.toiec.toiec.dto.request.chat.ChatBotRequest;
import com.toiec.toiec.dto.request.chat.ChatRequest;
import com.toiec.toiec.dto.request.chat.Message;
import com.toiec.toiec.dto.response.chat.ChatGptResponse;
import com.toiec.toiec.dto.response.chat.ChatResponse;
import com.toiec.toiec.entity.Chat;
import com.toiec.toiec.entity.User;
import com.toiec.toiec.exception.base.NotFoundException;
import com.toiec.toiec.repository.ChatRepository;
import com.toiec.toiec.repository.UserRepository;
import com.toiec.toiec.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    @Qualifier("openaiRestTemplate")
    private final RestTemplate restTemplate;
    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.url}")
    private String apiUrl;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    @Override
    public ChatGptResponse chatWithGpt(ChatBotRequest mess, String username)
    {
        Chat messageNew = new Chat();
        messageNew.setDate(LocalDateTime.now());
        messageNew.setMess(mess.getMessage());
        User userViq = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
        messageNew.setSender(userViq);
        Pageable pageable = PageRequest.of(0, 4);
        List<Chat>  chatList = chatRepository.findChatsByUsername(username,pageable);
        List<Message> messages = new ArrayList<>();
        for(Chat chat : chatList)
        {
            Message message = new Message();
            message.setContent(chat.getMess());
            if(chat.getSender()==null) message.setRole("system");
            else message.setRole("user");
            messages.add(message);
        }
        messages.add(new Message("user",mess.getMessage()));
        ChatRequest request = new ChatRequest(model, messages);
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        String reqText="";
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            reqText= "No response";
        }else{
            reqText=response.getChoices().get(0).getMessage().getContent();
            chatRepository.save(messageNew);
            Chat messageBotNew = new Chat();
            messageBotNew.setDate(LocalDateTime.now());
            messageBotNew.setMess(reqText);
            messageBotNew.setReceived(userViq);
            chatRepository.save(messageBotNew);
        }

        return new ChatGptResponse(reqText);
    }
}
