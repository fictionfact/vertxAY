package com.gigabyte.tugasakhir.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

public class BotHandler extends AbstractVerticle{
    private String request;
    public BotHandler(Message message){
        request = String.valueOf(message.body());

    }

    private void test(Message message) {
        System.out.println(message.body());
    }
}
