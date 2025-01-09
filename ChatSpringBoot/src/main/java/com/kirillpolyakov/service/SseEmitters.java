package com.kirillpolyakov.service;

import com.kirillpolyakov.model.Online;
import com.kirillpolyakov.model.User;
import com.kirillpolyakov.model.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class SseEmitters {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(SseEmitters.class);

    private final Map<Long, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();


    public SseEmitter add(SseEmitter emitter, long id) {
        this.userService.get(id);
        CopyOnWriteArrayList<SseEmitter> emitterList = this.emitters.getOrDefault(id, new CopyOnWriteArrayList<>());
        emitterList.add(emitter);
        this.emitters.put(id, emitterList);

        this.send(new Online(this.getOnline()));

        emitter.onCompletion(() -> {
            logger.info("Emitter completed: {}", emitter);
            this.emitters.get(id).remove(emitter);
            if (this.emitters.get(id).isEmpty()) {
                this.emitters.remove(id);
                this.send(new Online(this.getOnline()));
            }
        });
        emitter.onTimeout(() -> {
            logger.info("Emitter timed out: {}", emitter);
            emitter.complete();
            this.emitters.get(id).remove(emitter);
            if (this.emitters.get(id).isEmpty()) {
                this.emitters.remove(id);
                this.send(new Online(this.getOnline()));
            }
        });
        return emitter;
    }

    public void send(Object obj) {
        logger.info("Emitters current before deleting: {}", this.emitters);
        this.emitters.forEach((key, value) -> {
            Thread thread = new Thread(() -> value.forEach(x -> {
                try {
                    x.send(obj);
                } catch (Exception e) {
                    x.completeWithError(e);
                    value.remove(x);
                    if (value.isEmpty()) {
                        this.emitters.remove(key);
                    }
                    logger.error("Emitter failed: {}", x, e);
                }
            }));
            thread.start();
        });
        logger.info("Emitters current: {}", this.emitters);
    }

    public Set<String> getOnline() {
        return this.emitters.keySet().stream()
                .map(x -> this.userService.get(x).getUsername()).collect(Collectors.toSet());
    }

}
