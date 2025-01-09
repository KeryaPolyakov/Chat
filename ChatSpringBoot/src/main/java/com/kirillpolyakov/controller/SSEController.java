package com.kirillpolyakov.controller;

import com.kirillpolyakov.service.SseEmitters;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/sse")
public class SSEController {

    private final SseEmitters emitters;

    public SSEController(SseEmitters sseEmitters) {
        this.emitters = sseEmitters;
    }

    @GetMapping(path = "/chat/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getSubscription(@PathVariable long id) {
        return emitters.add(new SseEmitter(60000L), id);
    }

}
