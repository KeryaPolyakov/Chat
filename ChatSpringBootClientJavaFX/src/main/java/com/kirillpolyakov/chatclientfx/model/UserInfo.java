package com.kirillpolyakov.chatclientfx.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {

    private long id;

    private String username;

    private String password;
}
