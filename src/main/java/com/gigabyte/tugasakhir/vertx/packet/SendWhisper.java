package com.gigabyte.tugasakhir.vertx.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SendWhisper extends Message {
    private final String to;
    private final String whisperMessage;

    @JsonCreator
    public SendWhisper(@JsonProperty("to") String to, @JsonProperty("whisperMessage") String whisperMessage){
        this.to = to;
        this.whisperMessage = whisperMessage;
    }

    public String getTo() {
        return to;
    }

    public String getWhisperMessage() {
        return whisperMessage;
    }

    @Override
    public String toString() {
        return "SendWhisper{" +
                "to='" + to + '\'' +
                ", whisperMessage='" + whisperMessage + '\'' +
                '}';
    }
}
