package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Message;
import com.kirillpolyakov.model.User;
import com.kirillpolyakov.repository.MessageRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;


    private final UserService userService;

    private SseEmitters emitters;

    public MessageServiceImpl(MessageRepository messageRepository,
            UserService userService, SseEmitters emitters) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.emitters = emitters;
    }

    @Override
    public void add(Message message, long id) {
        User user = this.userService.get(id);
        message.setUser(user);
        this.messageRepository.save(message);
        this.emitters.send(message);
    }

    @Override
    public List<Message> getAll() {
        return this.messageRepository.findAll();
    }
}
