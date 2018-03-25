import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class ConnectionVerticle extends AbstractVerticle {
    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        // handle request berupa koneksi websocket
        server.websocketHandler(socket -> new ConnectionHandler(getVertx(), socket));

        // handle request http
        server.requestHandler(router::accept);

        router.get("/").handler(context -> {
            context.response().sendFile("Home.html");
        });

        server.requestHandler(router::accept);
        server.listen(8088);
    }

    private String processAnswer(AsyncResult<Message<Object>> reply){
        if(reply.succeeded()){
//            Answer a = JsonObject.mapFrom(reply.result().body()).mapTo(Answer.class);
//            String answer = a.getId() + " : " + a.getAnswers().stream().map(x -> x.toString()).collect(Collectors.joining(","));
//            return answer;
            String answer = (String) reply.result().body();
            return answer.toString();
        } else {
            return "Error getting result";
        }
    }
}
