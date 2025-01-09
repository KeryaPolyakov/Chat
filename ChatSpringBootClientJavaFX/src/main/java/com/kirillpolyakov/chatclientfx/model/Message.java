package com.kirillpolyakov.chatclientfx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Message {

    private long id;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime localDateTime = LocalDateTime.now();

    @NonNull
    private String message;

    private User user;

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return "Time:" + dateTimeFormatter.format(localDateTime) + "\n"
                + "message: " +message + "\n" + "username: " + user.getUsername();
    }
}
