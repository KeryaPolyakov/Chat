module com.kirillpolyakov.chatclientfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires okhttp.eventsource;
    requires static lombok;
    requires retrofit2;
    requires retrofit2.converter.jackson;
    requires java.prefs;
    requires okhttp3;


    opens com.kirillpolyakov.chatclientfx to javafx.fxml;
    exports com.kirillpolyakov.chatclientfx;
    opens com.kirillpolyakov.chatclientfx.controllers to javafx.fxml;
    exports com.kirillpolyakov.chatclientfx.model to com.fasterxml.jackson.databind;
    exports com.kirillpolyakov.chatclientfx.dto to com.fasterxml.jackson.databind;
    exports com.kirillpolyakov.chatclientfx.controllers to com.fasterxml.jackson.databind, javafx.fxml;
}