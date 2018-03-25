import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import model.User;

public class ConnectionHandler {
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
            map.forEach((x, y) -> sendToClient("/loggedIn " + x.toString()));
            subscribeTo("broadcast");
            sendToClient("Welcome to the chat room! Before you start chatting, please input your username by typing '/username (space) (your username)'");
        } else if (request.toLowerCase().startsWith("/username")){
            String name = request.split(" ", 2).length > 1 ? request.split(" ")[1] : "unknown";
            if (this.user == null) {
                login(name);
                sendToClient("Your username is " + name + ", now you can start chatting!");
            }
            else
                sendToClient("You already set your username");
        } else if (this.user == null){
            sendToClient("Please input your username by typing '/username (space) (your username)'");
        } else {
            broadcastMessage("/chat " + this.user.getUsername() + " " + request);
        }
    }

    private void subscribeTo(String address) {
        vertx.eventBus().consumer(address).handler(objectMessage -> {
            sendToClient(objectMessage.body().toString());
        });
    }

    private void sendToClient(String message) {
        System.out.println(Buffer.buffer(message));
        socket.write(Buffer.buffer(message));
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
            broadcastMessage("/loggedIn " + this.user.getUsername());
        } else {
            sendToClient("Username already taken, please input another username");
        }
    }

    private void broadcastMessage(String message) {
        vertx.eventBus().publish("broadcast", message);
    }
}
