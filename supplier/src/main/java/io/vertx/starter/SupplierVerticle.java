package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class SupplierVerticle extends AbstractVerticle {

  private static final String BORIS = "Boris";
  Logger LOG = LoggerFactory.getLogger(SupplierVerticle.class);
  int i = 1;

  @Override
  public void start() {
    String address = config().getString("address");
    LOG.info(BORIS + " is ready to send messages to the address: " + address);
    vertx.setPeriodic(5000,  event -> {
      JsonObject message = new JsonObject()
        .put("from", BORIS)
        .put("content", "Hello there!")
        .put("counter", i++);
      LOG.info(BORIS + " sends " + message);
      vertx.eventBus().publish("address", message);
    });
  }

}
