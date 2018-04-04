package com.gigabyte.tugasakhir.vertx.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WhisperMessage extends MyMessage {
    private final String whisperMessage;
    private final String from;

    @JsonCreator
    public WhisperMessage(@JsonProperty("from") String from, @JsonProperty("whisperMessage") String whisperMessage){
        this.whisperMessage = whisperMessage;
        this.from = from;
    }

    public String getWhisperMessage() {
        return whisperMessage;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "WhisperMessage{" +
                "whisperMessage='" + whisperMessage + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}
