package com.gigabyte.tugasakhir.vertx;

import com.gigabyte.tugasakhir.vertx.model.User;
import com.gigabyte.tugasakhir.vertx.packet.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
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
    public ConnectionHandler(Vertx vertx, ServerWebSocket socket) {
        this.vertx = vertx;
        this.socket = socket;
        this.user = null;

        // read message from network
        this.socket.handler(this::receiveMessage);

        // close connection handler
        this.socket.closeHandler(this::close);
    }

    private void close(Void aVoid) {
    }

    private void receiveMessage(Buffer buffer) {
        String request = String.valueOf(buffer);
        if (request.equals("/getUsers")){
            LocalMap<String, User> map = vertx.sharedData().getLocalMap("users");
            sendToClient(new UserList(map));
            subscribeTo("broadcast");
            sendToClient(new ChatNotification("Welcome to the chat room! Before you start chatting," +
                    " please input your username by typing '/username (space) (your username)'"));
        } else if (request.toLowerCase().startsWith("/username")) {
            if (request.split(" ", 2).length > 2){
                sendToClient(new ChatNotification("Username cannot include spaces."));
            } else if (request.split(" ", 2).length > 1) {
                String name = request.split(" ")[1];
                if (this.user == null) {
                    login(name);
                }
                else
                    sendToClient(new ChatNotification("You already set your username."));
            } else {
                sendToClient(new ChatNotification("Please input your username after '/username'."));
            }
        } else if (request.toLowerCase().startsWith("/whisper")) {
            String recipient = request.split(" ")[1];
            String whisperMessage = request.split(" ", 3)[2];
            whisperMessage(new WhisperMessage(this.user.getUsername(), whisperMessage), recipient);
        } else if (this.user == null){
            sendToClient(new ChatNotification("Please input your username by typing" +
                    " '/username (space) (your username)'."));
        } else if (request.toLowerCase().equals("/logout") || request.toLowerCase().equals("/quit")){
            logout();
        } else {
            broadcastMessage(new ChatMessage(this.user.getUsername(), request));
        }
    }

    private void logout(){
        sendToClient(new ChatNotification("You have left the chat room."));
        vertx.eventBus().consumer("user/" + this.user.getUsername()).unregister();
        vertx.eventBus().consumer("broadcast").unregister();
        LocalMap<String, User> map = vertx.sharedData().getLocalMap("users");
        map.remove(this.user.getUsername());
        broadcastMessage(new LogoutNotification(this.user));
        this.user = null;
    }

    private void subscribeTo(String address) {
        vertx.eventBus().consumer(address).handler(objectMessage -> {
            sendToClient(JsonObject.mapFrom(objectMessage.body()).mapTo(Message.class));
        });
    }

    private void sendToClient(Message message) {
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
            sendToClient(new ChatNotification("Your username is " + this.user.getUsername() + ", now you can start chatting!"));
        } else {
            sendToClient(new ChatNotification("Username already taken, please input another username"));
        }
    }

    private void broadcastMessage(Message message) {
        vertx.eventBus().publish("broadcast", JsonObject.mapFrom(message));
    }

    private void whisperMessage(Message message, String recipient){
        System.out.println(message);
        vertx.eventBus().publish("user/" + recipient, JsonObject.mapFrom(message));
    }
}
