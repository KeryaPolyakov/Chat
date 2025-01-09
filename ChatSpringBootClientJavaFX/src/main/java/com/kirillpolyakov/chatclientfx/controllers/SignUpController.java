package com.kirillpolyakov.chatclientfx.controllers;

import com.kirillpolyakov.chatclientfx.App;
import com.kirillpolyakov.chatclientfx.model.User;
import com.kirillpolyakov.chatclientfx.retrofit.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Arrays;

public class SignUpController {
    @FXML
    public Button buttonSignUp;
    @FXML
    public TextField textFieldLogin;
    @FXML
    public TextField textFieldPassword;
    @FXML
    public Button buttonSignIn;

    /**
     * Потверждение регистрации в чате с выводом сообщения с Id нового пользователя
     */
    @FXML
    public void buttonSignUp(ActionEvent actionEvent) {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        if (login.isEmpty()) {
            App.showInfo("Error", "login is empty", Alert.AlertType.INFORMATION);
        } else if (password.isEmpty()) {
            App.showInfo("Error", "password is empty", Alert.AlertType.INFORMATION);
        } else if (Arrays.stream(login.split("")).anyMatch(x -> !Character.isLetterOrDigit(x.charAt(0)))) {
            App.showInfo("Error", "login must be letters or digits", Alert.AlertType.INFORMATION);
        } else {
            User user = new User(login, password);
            try {
                new UserRepository().add(user);
                App.closeWindow(buttonSignUp);
                App.openWindow("main.fxml", "Authorization", null);
            } catch (IOException | IllegalArgumentException e) {
                App.showInfo("Error", e.getMessage(), Alert.AlertType.INFORMATION);
            }
        }
    }

    public void buttonSignIn(ActionEvent actionEvent) {
        try {
            App.closeWindow(buttonSignIn);
            App.openWindow("main.fxml", "Authorization", null);
        } catch (IOException e) {
            App.showInfo("Error", e.getMessage(), Alert.AlertType.INFORMATION);
        }
    }
}
