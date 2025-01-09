package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Message;

import java.util.List;

public interface MessageService {

    void add (Message message, long id);

    List<Message> getAll();
}
