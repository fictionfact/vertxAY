package com.gigabyte.tugasakhir.vertx;

import io.vertx.core.AbstractVerticle;
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
}
