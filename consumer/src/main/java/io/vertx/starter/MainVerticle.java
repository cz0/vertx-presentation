package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  private static final String STEPAN = "Stepan";
  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start() {
    String address = config().getString("address");
    LOG.info(STEPAN + " is ready to get messages on the address: " + address);
    vertx.eventBus().consumer(address, message -> {
      JsonObject messageBody = (JsonObject)message.body();
      LOG.info(STEPAN + " received message: " + messageBody);
      message.reply("Thanks for the message!");
      LOG.info(STEPAN + " sent reply");
    });
  }
}
