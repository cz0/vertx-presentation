package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.HashMap;

public class WebVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    System.out.println("Starting...");

    Router router = Router.router(vertx);

    BridgeOptions options = new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("address"));
    router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options));

    // Serve the static resources
    router.route().handler(StaticHandler.create());
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    vertx.setPeriodic(5000, t -> {
        HashMap<String, Object> news = new HashMap<>();
        news.put("News", "Vertx is cool");
        JsonObject data = new JsonObject(news);
        vertx.eventBus().publish("address", data);
      }
    );
  }

}
