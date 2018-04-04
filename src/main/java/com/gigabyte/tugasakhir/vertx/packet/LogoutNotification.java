package com.gigabyte.tugasakhir.vertx.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gigabyte.tugasakhir.vertx.model.User;

public class LogoutNotification extends MyMessage {
    private final User user;

    @JsonCreator
    public LogoutNotification(@JsonProperty("user") User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "LogoutNotification{" +
                "user=" + user +
                '}';
    }
}
