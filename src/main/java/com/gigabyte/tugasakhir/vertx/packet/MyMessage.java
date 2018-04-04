package com.gigabyte.tugasakhir.vertx.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="@class")
public class MyMessage {
    @JsonCreator
    public MyMessage() {
    }

    @Override
    public String toString() {
        return "MyMessage{}";
    }
}
