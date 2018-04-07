package com.gigabyte.tugasakhir.vertx;

import com.gigabyte.tugasakhir.vertx.model.QnA;
import com.gigabyte.tugasakhir.vertx.model.User;
import com.gigabyte.tugasakhir.vertx.packet.ChatMessage;
import com.gigabyte.tugasakhir.vertx.packet.MyMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import java.util.List;
import java.util.Random;

public class BotVerticle extends AbstractVerticle {
    private long currentTimer = 30;
    private int wait = 10, index;
    private QnA qna = new QnA();
    private List<List<String>> questionSet;
    private User user, bot;
    private String pesan;
    private String question, expectedAnswer, answer, username;
    private long timer;
    private Random r = new Random();
    @Override
    public void start() {
        bot = new User("Bot");
        LocalMap<String, User> map = vertx.sharedData().getLocalMap("users");
        map.putIfAbsent(this.bot.getUsername(), bot);
        questionSet = qna.getQuestionSet();
        setQuestion();
        broadcastMessage(new ChatMessage(this.bot.getUsername(), "PERTANYAAN : \n" + question));
        timer = vertx.setPeriodic(1000, this::startTimer);
        vertx.eventBus().consumer("answer").handler(this::answer);
        vertx.eventBus().consumer("requestScore").handler(this::broadcastScore);
        vertx.eventBus().consumer("requestQuestion").handler(this::requestQuestion);
    }

    private void startTimer(Long id) {
        currentTimer--;
        if (currentTimer == 0){
            broadcastMessage(new ChatMessage(this.bot.getUsername(), "Waktu telah habis! jawabannya adalah <font color=\'red\'>"
                    + expectedAnswer + "</font>."));
            newQuestion(id);
        } else if (currentTimer == 15 || currentTimer == 5){
            broadcastMessage(new ChatMessage(this.bot.getUsername(), "Waktu tinggal <font color=\'red\'>"
                    + currentTimer + " detik</font>."));
        }
    }

    private void newQuestion(Long id) {
        vertx.cancelTimer(id);

        broadcastScore();
        question = "";
        answer = "";
        broadcastMessage(new ChatMessage(this.bot.getUsername(), "Pertanyaan berikutnya dalam <font color=\"red\">" + wait + " detik.</font>"));
        vertx.setTimer(wait*1000, l -> {
            setQuestion();
            currentTimer = 30;
            broadcastMessage(new ChatMessage(this.bot.getUsername(), "<font color=\"red\">PERTANYAAN : </font>\n" + question));
            timer = vertx.setPeriodic(1000, this::startTimer);
        });
    }

    private void answer(Message message) {
        this.user = (User) message.body();
        answer = this.user.getMessage();
        username = this.user.getUsername();
        if (this.answer.toLowerCase().equals(expectedAnswer.toLowerCase())){
            broadcastMessage(new ChatMessage(this.bot.getUsername(), "<font color=\"blue\">" + username +
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
            if (!y.getUsername().equals(this.bot.getUsername()))
                pesan = pesan + "\n<font color=\'blue\'>" + y.getUsername() + "</font> <font color=\'red\'>(" + y.getScore() + " poin)</font>.";
        });
        broadcastMessage(new ChatMessage(this.bot.getUsername(), pesan));
    }

    private void requestQuestion(Message<Object> objectMessage) {
        if (!question.equals(""))
            broadcastMessage(new ChatMessage(this.bot.getUsername(), "<font color=\"blue\">(PENGULANGAN PERTANYAAN)</font>\n" + question));
        else
            broadcastMessage(new ChatMessage(this.bot.getUsername(), "Silakan menunggu hingga pertanyaan diberikan."));
    }


    private void broadcastMessage(MyMessage message) {
        vertx.eventBus().publish("broadcast", JsonObject.mapFrom(message));
    }

    private void setQuestion(){
        index = r.nextInt(questionSet.size());
        question = questionSet.get(index).get(0);
        expectedAnswer = questionSet.get(index).get(1);
    }
}
