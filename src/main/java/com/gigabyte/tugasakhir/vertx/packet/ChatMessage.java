package com.gigabyte.tugasakhir.vertx.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class ChatMessage extends MyMessage {
    private final String chatMessage;
    private final String from;

    @JsonCreator
    public ChatMessage(@JsonProperty("from") String from, @JsonProperty("chatMessage") String chatMessage){
        this.chatMessage = chatMessage;
        this.from = from;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "chatMessage='" + chatMessage + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}
