package io.vertx.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunnerWithParametersFactory;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
@Parameterized.UseParametersRunnerFactory(VertxUnitRunnerWithParametersFactory.class)
public class MainVerticleTest {
  private Vertx vertx;
  private final String targetCountry;
  private RedisClient redis;

  @Parameterized.Parameters
  public static Iterable<String> targetCountries() {
    return Arrays.asList("Czech Republic");
  }

  public MainVerticleTest(String targetCountry) {
    this.targetCountry = targetCountry;
  }

  @Before
  public void before(TestContext context) {
    vertx = Vertx.vertx();

    vertx.deployVerticle(MainVerticle.class.getName(), new DeploymentOptions(), context.asyncAssertSuccess());

    redis = RedisClient.create(vertx, new RedisOptions().setHost(MainVerticle.HOST));
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void talkToRedis(TestContext context) {
    vertx.eventBus().publish(MainVerticle.ADDRESS, new JsonObject()
      .put("content", "Hello, " + targetCountry)
      .put("counter", targetCountry.length()));

    Async async = context.async();

    redis.hgetall("key:" + targetCountry.length(), asyncResult -> {
      if (asyncResult.succeeded()) {
        if (asyncResult.succeeded()) {
          context.assertEquals("Hello, " + targetCountry,
            asyncResult.result().getString("content"));
        } else {
          System.out.println(asyncResult.cause().getMessage());
        }
        async.complete();
      }
    });
  }
}
