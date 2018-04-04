package com.gigabyte.tugasakhir.vertx.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatNotification extends MyMessage {
    private final String notification;

    @JsonCreator
    public ChatNotification(@JsonProperty("notification") String notification){
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }

    @Override
    public String toString() {
        return "ChatNotification{" +
                "notification='" + notification + '\'' +
                '}';
    }
}
