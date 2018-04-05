package com.gigabyte.tugasakhir.vertx;

import com.gigabyte.tugasakhir.vertx.packet.ChatMessage;
import com.gigabyte.tugasakhir.vertx.packet.MyMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import com.gigabyte.tugasakhir.vertx.model.User;
import io.vertx.core.shareddata.LocalMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotVerticle extends AbstractVerticle {
    private long currentTimer = 30;
    private int wait = 10, index;
    private List<List<String>> questionSet;
    private List<String> qna;
    private User user;
    private String pesan;
    private String question, expectedAnswer, answer, username;
    private long timer;
    private Random r = new Random();
    @Override
    public void start() {
        generateQuestion();
        broadcastMessage(new ChatMessage("Bot", "PERTANYAAN : \n" + question));
        timer = vertx.setPeriodic(1000, this::startTimer);
        vertx.eventBus().consumer("answer").handler(this::answer);
        vertx.eventBus().consumer("requestScore").handler(this::broadcastScore);
        vertx.eventBus().consumer("requestQuestion").handler(this::requestQuestion);
    }

    private void startTimer(Long id) {
        currentTimer--;
        if (currentTimer == 0){
            broadcastMessage(new ChatMessage("Bot", "Waktu telah habis! jawabannya adalah <font color=\'red\'>"
                    + expectedAnswer + "</font>."));
            newQuestion(id);
        } else if (currentTimer == 15 || currentTimer == 5){
            broadcastMessage(new ChatMessage("Bot", "Waktu tinggal " + currentTimer + " detik."));
        }
    }

    private void newQuestion(Long id) {
        vertx.cancelTimer(id);

        broadcastScore();
        question = "";
        answer = "";
        broadcastMessage(new ChatMessage("Bot", "Pertanyaan berikutnya dalam <font color=\"red\">" + wait + " detik.</font>"));
        vertx.setTimer(wait*1000, l -> {
            setQuestion();
            currentTimer = 30;
            broadcastMessage(new ChatMessage("Bot", "<font color=\"red\">PERTANYAAN : </font>\n" + question));
            timer = vertx.setPeriodic(1000, this::startTimer);
        });
    }

    private void answer(Message message) {
        this.user = (User) message.body();
        answer = this.user.getMessage();
        username = this.user.getUsername();
        if (this.answer.toLowerCase().equals(expectedAnswer.toLowerCase())){
            broadcastMessage(new ChatMessage("Bot", "<font color=\"blue\">" + username +
                    "</font> menjawab dengan benar! <font color=\"red\">+10 point</font> untuk <font color=\"blue\">" +
                    username + "</font>."));
            this.user.setScore(10);
            newQuestion(timer);
        }
    }

    private void broadcastScore(Message<Object> objectMessage) {
        broadcastScore();
    }

    private void broadcastScore(){
        pesan = "Daftar user dan score : ";
        LocalMap<String, User> map = vertx.sharedData().getLocalMap("users");
        map.forEach((x, y) -> {
            pesan = pesan + "\n" + y.getUsername() + " (" + y.getScore() + " poin).";
        });
        broadcastMessage(new ChatMessage("Bot", pesan));
    }

    private void requestQuestion(Message<Object> objectMessage) {
        if (!question.equals(""))
            broadcastMessage(new ChatMessage("Bot", "<font color=\"blue\">(PENGULANGAN PERTANYAAN)</font>\n" + question));
        else
            broadcastMessage(new ChatMessage("Bot", "Silakan menunggu hingga pertanyaan diberikan."));
    }


    private void broadcastMessage(MyMessage message) {
        vertx.eventBus().publish("broadcast", JsonObject.mapFrom(message));
    }

    private void generateQuestion(){
        questionSet = new ArrayList<>();

        qna = new ArrayList<>();
        qna.add(0, "Siapakah Presiden kedua Negara Indonesia?");
        qna.add(1, "Soeharto");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Siapakah manusia pertama yang menyentuh bulan?");
        qna.add(1, "Neil Armstrong");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Tuliskan nama ilmiah dari air putih!");
        qna.add(1, "H2O");
        questionSet.add(qna);

        setQuestion();
    }

    private void setQuestion(){
        index = r.nextInt(questionSet.size());
        question = questionSet.get(index).get(0);
        expectedAnswer = questionSet.get(index).get(1);
    }
}
