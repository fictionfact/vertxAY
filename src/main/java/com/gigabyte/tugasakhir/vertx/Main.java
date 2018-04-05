package com.gigabyte.tugasakhir.vertx;

import io.vertx.core.Vertx;

public class Main {
    public static void main(String args[]){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ConnectionVerticle());
//        vertx.deployVerticle(new BotHandler());
        vertx.deployVerticle(new BotVerticle());
    }
}
