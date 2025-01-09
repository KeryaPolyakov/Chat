package com.kirillpolyakov.chatclientfx.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.chatclientfx.App;
import com.kirillpolyakov.chatclientfx.model.Message;
import com.kirillpolyakov.chatclientfx.model.Online;
import com.kirillpolyakov.chatclientfx.model.UserInfo;
import com.kirillpolyakov.chatclientfx.retrofit.MessageRepository;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.MessageEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class ChatController implements ControllerData<UserInfo> {

    @FXML
    public TextField textFieldWriteMessage;
    @FXML
    public TextArea textAreaGetMessage;
    @FXML
    public Button buttonExit;
    @FXML
    public ListView<String> listViewOnlineUsers;

    private MessageRepository messageRepository;

    private UserInfo userDetails;

    /**
     * Поток обеспечивающий подписку на сервер с переодичностью 1 минута
     */
    private Thread thread;

    /**
     * Инициализация всех сообщений которые были в чате
     */
    @Override
    public void initData(UserInfo value) {
        userDetails = value;
        messageRepository = new MessageRepository(userDetails.getUsername(), userDetails.getPassword());
        thread = new Thread(() -> {
            try {
                while (true) {
                    String url = "http://localhost:8080/sse/chat/" + userDetails.getId();
                    EventSource.Builder builder = new EventSource.Builder(new EventHandler() {

                        final ObjectMapper objectMapper = new ObjectMapper();

                        {
                            objectMapper.registerModule(new JavaTimeModule());
                        }

                        @Override
                        public void onOpen() {
                            System.out.println("onOpen");
                        }

                        @Override
                        public void onClosed() {
                            System.out.println("onClosed");
                        }

                        @Override
                        public void onMessage(String event, MessageEvent messageEvent) {
                            try {
                                Message message = objectMapper.readValue(messageEvent.getData(), Message.class);
                                textAreaGetMessage.setText(textAreaGetMessage.getText() + "\n\n"
                                        + message);
                                textAreaGetMessage.setScrollTop(Double.MAX_VALUE);
                            } catch (JsonProcessingException e) {
                                try {
                                    Online online = objectMapper.readValue(messageEvent.getData(), Online.class);
                                    List<String> list = online.getOnlineUsers().stream()
                                            .filter(x -> !x.equals(userDetails.getUsername())).toList();
                                    Platform.runLater(() -> listViewOnlineUsers.setItems(FXCollections.observableList(list)));
                                } catch (JsonProcessingException ignored) {
                                }
                            }
                        }

                        @Override
                        public void onComment(String comment) {
                            System.out.println("onComment");
                        }

                        @Override
                        public void onError(Throwable t) {
                            System.out.println("onError: " + t);
                            t.printStackTrace();
                        }
                    }, URI.create(url));
                    try (EventSource eventSource = builder.build()) {
                        eventSource.start();
                        TimeUnit.MINUTES.sleep(1);
                    }
                }
            } catch (InterruptedException ignored) {
            }
        });
        try {
            List<Message> messages = messageRepository.getAll();
            StringJoiner stringJoiner = new StringJoiner("\n\n");
            messages.forEach(x -> stringJoiner.add(x.toString()));
            textAreaGetMessage.setText(stringJoiner.toString());
            thread.setDaemon(true);
            thread.start();
        } catch (Exception e) {
            App.showInfo("info", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Отправка сообщения в чат
     */
    @FXML
    public void buttonSend(ActionEvent actionEvent) throws IOException {
        Message message = new Message(textFieldWriteMessage.getText());
        messageRepository.add(message, userDetails.getId());
        textFieldWriteMessage.setText("");
    }

    /**
     * Выход из чата с остановкой потока подписки
     **/
    public void buttonExit(ActionEvent actionEvent) {
        thread.interrupt();
        try {
            App.openWindow("main.fxml", "Main", null);
            App.closeWindow(buttonExit);
        } catch (IOException ignored) {
        }
    }

}
