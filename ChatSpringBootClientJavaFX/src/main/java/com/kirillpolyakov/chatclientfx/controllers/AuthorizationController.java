package com.kirillpolyakov.chatclientfx.controllers;

import com.kirillpolyakov.chatclientfx.App;
import com.kirillpolyakov.chatclientfx.model.User;
import com.kirillpolyakov.chatclientfx.model.UserInfo;
import com.kirillpolyakov.chatclientfx.retrofit.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Arrays;

public class AuthorizationController {
    @FXML
    public Button buttonLogIn;
    @FXML
    public Button buttonSignUp;
    @FXML
    public TextField textFieldUsername;
    @FXML
    public TextField textFieldPassword;

    /**
     * В ход в чат
     */
    @FXML
    public void buttonLogIn(ActionEvent actionEvent) throws IOException {
        String login = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        if (login.isEmpty()) {
            App.showInfo("Error", "Login is empty", Alert.AlertType.ERROR);
        } else if (password.isEmpty()) {
            App.showInfo("Error", "Password is empty", Alert.AlertType.ERROR);
        } else if (Arrays.stream(login.split("")).anyMatch(x -> !Character.isLetterOrDigit(x.charAt(0)))) {
            App.showInfo("Error", "Login must be digit or letter", Alert.AlertType.ERROR);
        } else {
            try {
                User user = new UserRepository(login, password).authenticate();
                App.openWindow("chat.fxml", user.getUsername(), new UserInfo(user.getId(), user.getUsername(),
                        textFieldPassword.getText()));
                Stage stage = (Stage) buttonLogIn.getScene().getWindow();
                stage.close();
            } catch (IllegalArgumentException e) {
                App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
            } catch (ConnectException e) {
                App.showInfo("Error", "No connection with server", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Регистрация пользователя в чате
     */
    @FXML
    public void buttonSignUp(ActionEvent actionEvent) {
        try {
            App.closeWindow(buttonSignUp);
            App.openWindow("signUp.fxml", "Sign up", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

