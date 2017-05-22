package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static final String ADDRESS = "address";
  public static final String HOST = "localhost";


  @Override
  public void start() {
    RedisClient redis = RedisClient.create(vertx, new RedisOptions().setHost(HOST));

    LOG.info("Redis is ready to get messages on the address: " + ADDRESS);

    vertx.eventBus().consumer(ADDRESS, message -> {
      JsonObject messageBody = (JsonObject) message.body();

      LOG.info("Redis received message: " + messageBody);

      redis.hmset("key:" + messageBody.getInteger("counter"), messageBody, res -> {
        if (res.succeeded()) {
          LOG.info("Value stored");
        } else {
          LOG.info(res.cause().getMessage());
        }
      });
    });
  }

}
