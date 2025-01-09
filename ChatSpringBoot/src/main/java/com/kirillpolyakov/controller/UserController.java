package com.kirillpolyakov.controller;

import com.kirillpolyakov.dto.ResponseResult;
import com.kirillpolyakov.model.User;
import com.kirillpolyakov.model.UserDetailsImpl;
import com.kirillpolyakov.service.SseEmitters;
import com.kirillpolyakov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("user")
public class UserController {

    private UserService userService;

    private SseEmitters emitters;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setEmitters(SseEmitters emitters) {
        this.emitters = emitters;
    }

    @PostMapping
    public ResponseEntity<ResponseResult<User>> add(@RequestBody User user) {
        this.userService.add(user);
        return new ResponseEntity<>(new ResponseResult<>(null, user), HttpStatus.OK);
    }

    @GetMapping("authentication")
    public ResponseEntity<ResponseResult<User>> authenticate(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            long id = ((UserDetailsImpl) authentication.getPrincipal()).getId();
            return new ResponseEntity<>(new ResponseResult<>(null, this.userService.get(id)), HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("online")
    public ResponseEntity<ResponseResult<Set<String>>> getOnline() {
        return new ResponseEntity<>(new ResponseResult<>(null, this.emitters.getOnline()), HttpStatus.OK);
    }
}
