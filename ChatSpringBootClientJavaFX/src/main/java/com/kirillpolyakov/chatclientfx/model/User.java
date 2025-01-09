package com.kirillpolyakov.chatclientfx.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

    private long id;

    @NonNull
    private String username;

    @NonNull
    private String password;
}
