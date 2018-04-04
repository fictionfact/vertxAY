package com.gigabyte.tugasakhir.vertx;

import com.gigabyte.tugasakhir.vertx.packet.ChatMessage;
import com.gigabyte.tugasakhir.vertx.packet.MyMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class BotVerticle extends AbstractVerticle {
    private long start, wait = 30 * 1000, end;
    private boolean answered = false;
    private String question, answer;
    @Override
    public void start() throws InterruptedException {
        Thread.sleep(10 * 1000);
        automate();
        vertx.eventBus().consumer("answer").handler(this::answer);
    }

    private void answer(Message message) {
        this.answer = (String) message.body();
        System.out.println(this.answer);
    }

    private void automate() throws InterruptedException {
        question = "Siapakah Presiden pertama Negara Indonesia?";
        answer = "Soekarno";
//        broadcastMessage(new ChatMessage("Bot", "Pertanyaan berikutnya 30 detik lagi."));
//        Thread.sleep(15 * 1000);
//        broadcastMessage(new ChatMessage("Bot", "Pertanyaan berikutnya 15 detik lagi."));
//        Thread.sleep(10 * 1000);
//        broadcastMessage(new ChatMessage("Bot", "Pertanyaan berikutnya 5 detik lagi."));
//        Thread.sleep(5 * 1000);
        broadcastMessage(new ChatMessage("Bot", "PERTANYAAN : \n" + question));
//        start = System.currentTimeMillis();
//        end = start + end;
//        while(start < end){
//
//        }
        while(true){
            if (this.answer.equals(answer.toLowerCase())){
                break;
            }
        }

        broadcastMessage(new ChatMessage("Bot", "BENAR SEKALI!"));
    }

    private void broadcastMessage(MyMessage message) {
        vertx.eventBus().publish("broadcast", JsonObject.mapFrom(message));
    }
}
