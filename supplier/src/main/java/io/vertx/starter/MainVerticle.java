package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/person").handler(req -> {
      String boris = Json.encode(new Person("Boris", 35));
      req.response().end(boris);
    });
    router.post("/person").handler(req -> {
      JsonObject json = req.getBodyAsJson();
      vertx.eventBus().publish("topic-1", json);
      req.response().end("Success");
    });
    router.get("/home").handler(req -> req.response().end("Home page"));

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }

  static class Person {
    private String name;
    private int age;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public Person() {
    }

    public Person(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

}
