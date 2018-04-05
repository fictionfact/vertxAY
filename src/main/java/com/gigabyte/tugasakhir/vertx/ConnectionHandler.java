package com.gigabyte.tugasakhir.vertx;

import com.gigabyte.tugasakhir.vertx.codec.userMessageCodec;
import com.gigabyte.tugasakhir.vertx.model.User;
import com.gigabyte.tugasakhir.vertx.packet.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;

/*
    to be done:
    - whisper message
    - test after logout message behaviour
 */

public class  ConnectionHandler {
    private final Vertx vertx;
    private final ServerWebSocket socket;
    private User user;
    private DeliveryOptions options;
    public ConnectionHandler(Vertx vertx, ServerWebSocket socket) {
        this.vertx = vertx;
        this.socket = socket;
        this.user = null;

        MessageCodec myCodec = new userMessageCodec();

        options = new DeliveryOptions().setCodecName(myCodec.name());

        try {
            vertx.eventBus().registerCodec(myCodec);
        } catch (Exception e) {
            System.out.println(e);
        }

        // read message from network
        this.socket.handler(this::receiveMessage);

        // close connection handler
        this.socket.closeHandler(this::close);
    }

    private void close(Void aVoid) {
    }

    private void receiveMessage(Buffer buffer) {
        String request = String.valueOf(buffer);
        if (request.replaceAll("\\s+", "").equals("")){
//            sendToClient(new ChatNotification("Please write something before sending."));
        } else if (request.equals("/getUsers")){
            LocalMap<String, User> map = vertx.sharedData().getLocalMap("users");
            sendToClient(new UserList(map));
            subscribeTo("broadcast");
            sendToClient(new ChatNotification("Selamat datang! Sebelum anda memulai chatting," +
                    " silakan menginput username anda dengan mengetik '/username (spasi) (username anda)'"));
        } else if (request.toLowerCase().startsWith("/username")) {
            if (request.split(" ").length == 2 && !request.split(" ", 2)[1].contains(" ")) {
                String name = request.split(" ")[1];
                if (this.user == null) {
                    login(name);
                }
                else
                    sendToClient(new ChatNotification("Anda telah memiliki username."));
            } else if (request.split(" ").length == 1){
                sendToClient(new ChatNotification("Silakan menginput username anda setelah '/username'."));
            } else {
                sendToClient(new ChatNotification("Username tidak boleh memiliki spasi."));
            }
        } else if (this.user == null){
            sendToClient(new ChatNotification("Silakan menginput username anda dengan mengetik" +
                    " '/username (spasi) (username anda)'."));
        } else if (request.toLowerCase().startsWith("/whisper")) {
            if (request.split(" ").length >= 3) {
                String recipient = request.split(" ")[1];
                String whisperMessage = request.split(" ", 3)[2];
                LocalMap<String, User> map = vertx.sharedData().getLocalMap("users");
                if (map.containsKey(recipient)) {
                    whisperMessage(new WhisperMessage(this.user.getUsername(), whisperMessage), recipient);
                    sendToClient(new SendWhisper(recipient, whisperMessage));
                } else sendToClient(new ChatNotification("User " + recipient + " tidak ditemukan."));
            } else {
                sendToClient(new ChatNotification("Whisper command gagal, silakan mengirim whisper dengan mengetik " +
                        "'/whisper (spasi) (user yang ingin diwhisper) (spasi) (pesan)'."));
            }
        } else if (request.toLowerCase().equals("/logout") || request.toLowerCase().equals("/quit")){
            broadcastMessage(new ChatMessage(this.user.getUsername(), request));
            logout();
            if (request.toLowerCase().equals("/quit")) {
                sendToClient(new ChatNotification("Kamu telah meninggalkan chat room."));
            }
        } else if (request.toLowerCase().equals("/score")){
            broadcastMessage(new ChatMessage(this.user.getUsername(), request));
            vertx.eventBus().send("requestScore", "");
        } else if (request.toLowerCase().equals("/question")){
            broadcastMessage(new ChatMessage(this.user.getUsername(), request));
            vertx.eventBus().send("requestQuestion", "");
        } else {
            broadcastMessage(new ChatMessage(this.user.getUsername(), request));
//            Message<String> test = this.user.getUsername();
            this.user.setMessage(request);
            vertx.eventBus().send("answer", this.user, options);
        }
    }

    private void logout(){
        vertx.eventBus().consumer("user/" + this.user.getUsername()).unregister();
        LocalMap<String, User> map = vertx.sharedData().getLocalMap("users");
        map.remove(this.user.getUsername());
        broadcastMessage(new LogoutNotification(this.user));
        this.user = null;
    }

    private void subscribeTo(String address) {
        vertx.eventBus().consumer(address).handler(objectMessage -> {
            sendToClient(JsonObject.mapFrom(objectMessage.body()).mapTo(MyMessage.class));
        });
    }

    private void sendToClient(MyMessage message) {
        socket.write(JsonObject.mapFrom(message).toBuffer());
    }

    private void login(String username) {
        LocalMap<String, User> map = vertx.sharedData().getLocalMap("users");
        User user = new User(username);
        if (map.putIfAbsent(username, user) == null) {
            this.user = user;

            //subscribe to broadcast
//            subscribeTo("broadcast");

            // register handler to user/<username> to send the message to network client
            subscribeTo("user/" + this.user.getUsername());

            // broadcast login
            broadcastMessage(new LoginNotification(this.user));
            sendToClient(new ChatNotification("Username anda adalah " + this.user.getUsername() + ", anda sudah bisa mulai chatting!"));
        } else {
            sendToClient(new ChatNotification("Username telah terambil, silakan menginput username lain"));
        }
    }

    private void broadcastMessage(MyMessage message) {
        vertx.eventBus().publish("broadcast", JsonObject.mapFrom(message));
    }

    private void whisperMessage(MyMessage message, String recipient){
        System.out.println(message);
        vertx.eventBus().publish("user/" + recipient, JsonObject.mapFrom(message));
    }
}
