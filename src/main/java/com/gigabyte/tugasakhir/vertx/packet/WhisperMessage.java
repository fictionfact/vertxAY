package com.gigabyte.tugasakhir.vertx.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WhisperMessage extends Message {
    private final String chatMessage;
    private final String from;

    @JsonCreator
    public WhisperMessage(@JsonProperty("from") String from, @JsonProperty("chatMessage") String chatMessage){
        this.chatMessage = chatMessage;
        this.from = from;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    @Override
    public String toString() {
        return "WhisperMessage{" +
                "chatMessage='" + chatMessage + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}
