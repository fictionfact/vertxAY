package com.gigabyte.tugasakhir.vertx.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gigabyte.tugasakhir.vertx.model.User;
import io.vertx.core.http.ServerWebSocket;

public class LoginNotification extends Message {
    private final User user;
    private final ServerWebSocket socket;

    @JsonCreator
    public LoginNotification(@JsonProperty("user") User user, @JsonProperty("isTheUser") ServerWebSocket socket) {
        this.user = user;
        this.socket = socket;
    }

    public User getUser() {
        return user;
    }

    public ServerWebSocket getSocket() {
        return socket;
    }

    @Override
    public String toString() {
        return "LoginNotification{" +
                "user=" + user +
                ", socket=" + socket +
                '}';
    }
}
