package com.kirillpolyakov.chatclientfx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Online {

    private Set<String> onlineUsers;
}
