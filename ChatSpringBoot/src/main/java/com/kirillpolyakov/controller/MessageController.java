package com.kirillpolyakov.controller;

import com.kirillpolyakov.dto.ResponseResult;
import com.kirillpolyakov.model.Message;
import com.kirillpolyakov.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<ResponseResult<Message>> add(@RequestBody Message message,
                                                       @PathVariable long id) {
        this.messageService.add(message, id);
        return new ResponseEntity<>(new ResponseResult<>(null, message), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseResult<List<Message>>> get() {
        return new ResponseEntity<>(new ResponseResult<>(null, this.messageService.getAll()), HttpStatus.OK);
    }

}
