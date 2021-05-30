package com.example.distributed.eb.eb_server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class SenderVerticle extends AbstractVerticle {

  private  static final Logger logger = LoggerFactory.getLogger(SenderVerticle.class);


  @Override
  public void start(Promise<Void> promise) {
    final var router = Router.router(vertx);

    router.route("/").handler(routingContext->{
      HttpServerResponse response = routingContext.response();
      response.putHeader("content-type", "application/json; charset=utf-8").end("<h1>" + "Hello from clustered messenger example!" + "</h1>");
    });



    router.post("/sendForAll/:" + "message").handler(this::sendMessageForAllReceivers);
    createAnHttpServer(vertx, router, config(),8080, promise);



  }

  static void createAnHttpServer(final Vertx vertx, final Router router, final JsonObject config,
                                        final int port, final Promise<Void> promise){
    vertx.createHttpServer().requestHandler(router)
      .listen(config.getInteger("http.port", port), result -> {
        if (result.succeeded()) {
          logger.info("HTTP server running on port {}", port);
          promise.complete();
        } else {
          logger.error("Could not start a HTTP server ", result.cause());
          promise.fail(result.cause());
        }
      });
  }

  private void sendMessageForAllReceivers(RoutingContext routingContext){
    final EventBus eventBus = vertx.eventBus();
    final String message = routingContext.request().getParam("message");
    eventBus.publish("kodcu.com", message);
    logger.info("Current Thread Id {} Is Clustered {} ",
      Thread.currentThread().getId(), vertx.isClustered());
    routingContext.response().end(message);
  }

}
